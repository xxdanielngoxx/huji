import { Environment, Stack, StackProps, Tags } from "aws-cdk-lib";
import {
  InstanceClass,
  InstanceSize,
  InstanceType,
  IpAddresses,
  ISecurityGroup,
  IVpc,
  Peer,
  Port,
  SecurityGroup,
  SubnetType,
  Vpc,
} from "aws-cdk-lib/aws-ec2";
import { Construct } from "constructs";
import {
  APPLICATION_TAG_KEY,
  APPLICATION_TAG_VALUE,
  ENVIRONMENT_TAG_KEY,
} from "../constant/tag.constant";
import {
  ApplicationListener,
  ApplicationLoadBalancer,
  ApplicationProtocol,
  IApplicationLoadBalancer,
} from "aws-cdk-lib/aws-elasticloadbalancingv2";
import {
  Cluster,
  ContainerImage,
  FargateService,
  FargateTaskDefinition,
  ICluster,
  IService,
  ListenerConfig,
  LogDrivers,
  Protocol,
  TaskDefinition,
  Secret as EcsSecret,
} from "aws-cdk-lib/aws-ecs";
import { IRepository, Repository } from "aws-cdk-lib/aws-ecr";
import { RetentionDays } from "aws-cdk-lib/aws-logs";
import { StringParameter } from "aws-cdk-lib/aws-ssm";
import {
  DatabaseInstance,
  DatabaseInstanceEngine,
  IDatabaseInstance,
  PostgresEngineVersion,
} from "aws-cdk-lib/aws-rds";
import { Secret } from "aws-cdk-lib/aws-secretsmanager";

export const INGRESS_SUBNETS_GROUP_NAME = "Ingress";

export const ECS_CLUSTER_SUBNETS_GROUP_NAME = "EcsCluster";

export const RDS_SUBNETS_GROUP_NAME = "Rds";

export const CONTAINER_NAME = "ApiContainer";

export const DATABASE_NAME = "huji";

export interface MainStackProps extends StackProps {
  readonly environmentName: string;
  readonly imageTag: string;
  readonly env: Environment;
}

export class MainStack extends Stack {
  private readonly vpc: IVpc;

  private readonly cluster: ICluster;

  private readonly alb: IApplicationLoadBalancer;

  private readonly albHttpListener: ApplicationListener;

  private readonly albIngressSecurityGroup: ISecurityGroup;

  private readonly service: IService;

  private readonly serviceIngressSecurityGroup: ISecurityGroup;

  private readonly databaseInstance: IDatabaseInstance;

  private readonly postgresSecretTemplate: Secret;

  constructor(scope: Construct, props: MainStackProps) {
    super(scope, "MainStack", props);

    this.vpc = this.createVpc();

    this.cluster = this.createEcsCluster({ vpc: this.vpc });

    this.albIngressSecurityGroup = this.createAlbIngressSecurityGroup({
      vpc: this.vpc,
    });

    this.alb = this.createAlb({
      vpc: this.vpc,
      ingressSecurityGroup: this.albIngressSecurityGroup,
    });

    this.albHttpListener = this.createAlbHttpListener({ alb: this.alb });

    this.serviceIngressSecurityGroup = this.createServiceIngressSecurityGroup({
      vpc: this.vpc,
      albIngressSecurityGroup: this.albIngressSecurityGroup,
    });

    this.postgresSecretTemplate = new Secret(this, "PostgresSecretTemplate", {
      generateSecretString: {
        secretStringTemplate: JSON.stringify({ username: "huji" }),
        generateStringKey: "password",
        excludeCharacters: '/@"',
      },
    });

    this.databaseInstance = this.createPostgresDatabaseInstance({
      vpc: this.vpc,
      serviceIngressSecurityGroup: this.serviceIngressSecurityGroup,
      postgresSecretTemplate: this.postgresSecretTemplate,
    });

    this.service = this.createService({
      vpc: this.vpc,
      cluster: this.cluster,
      albHttpListener: this.albHttpListener,
      serviceIngressSecurityGroup: this.serviceIngressSecurityGroup,
      imageTag: props.imageTag,
    });

    this.createTags(props);
  }

  private createVpc(): IVpc {
    return new Vpc(this, "Vpc", {
      ipAddresses: IpAddresses.cidr("10.0.0.0/16"),
      maxAzs: 2,
      natGateways: 1,
      subnetConfiguration: [
        {
          name: INGRESS_SUBNETS_GROUP_NAME,
          cidrMask: 24,
          subnetType: SubnetType.PUBLIC,
        },
        {
          name: ECS_CLUSTER_SUBNETS_GROUP_NAME,
          cidrMask: 24,
          subnetType: SubnetType.PRIVATE_WITH_EGRESS,
        },
        {
          name: RDS_SUBNETS_GROUP_NAME,
          cidrMask: 28,
          subnetType: SubnetType.PRIVATE_ISOLATED,
        },
      ],
    });
  }

  private createEcsCluster({ vpc }: { vpc: IVpc }): ICluster {
    const cluster: ICluster = new Cluster(this, "EcsCluster", {
      vpc,
    });

    return cluster;
  }

  private createAlb({
    vpc,
    ingressSecurityGroup,
  }: {
    vpc: IVpc;
    ingressSecurityGroup: ISecurityGroup;
  }): IApplicationLoadBalancer {
    const alb: IApplicationLoadBalancer = new ApplicationLoadBalancer(
      this,
      "Alb",
      {
        vpc,
        internetFacing: true,
        securityGroup: ingressSecurityGroup,
      }
    );

    return alb;
  }

  private createAlbHttpListener({
    alb,
  }: {
    alb: IApplicationLoadBalancer;
  }): ApplicationListener {
    return alb.addListener("HttpListener", {
      port: 80,
    });
  }

  private createAlbIngressSecurityGroup({
    vpc,
  }: {
    vpc: IVpc;
  }): ISecurityGroup {
    const ingressSecurityGroup: ISecurityGroup = new SecurityGroup(
      this,
      "AlbIngressSecurityGroup",
      {
        vpc,
      }
    );

    ingressSecurityGroup.addIngressRule(
      Peer.anyIpv4(),
      Port.HTTP,
      "Allow all inbound ipv4 HTTP traffic"
    );

    ingressSecurityGroup.addIngressRule(
      Peer.anyIpv4(),
      Port.HTTPS,
      "Allow all inbound ipv4 HTTPS traffic"
    );

    return ingressSecurityGroup;
  }

  private createService({
    vpc,
    cluster,
    albHttpListener,
    serviceIngressSecurityGroup,
    imageTag,
  }: {
    vpc: IVpc;
    cluster: ICluster;
    albHttpListener: ApplicationListener;
    serviceIngressSecurityGroup: ISecurityGroup;
    imageTag: string;
  }): IService {
    const taskDefinition: TaskDefinition = this.createTaskDefinition(imageTag);

    const service = new FargateService(this, "Service", {
      cluster: cluster,
      taskDefinition,
      vpcSubnets: vpc.selectSubnets({
        subnetGroupName: ECS_CLUSTER_SUBNETS_GROUP_NAME,
      }),
      securityGroups: [serviceIngressSecurityGroup],
    });

    service.registerLoadBalancerTargets({
      containerName: CONTAINER_NAME,
      newTargetGroupId: "ECS",
      containerPort: 8080,
      protocol: Protocol.TCP,
      listener: ListenerConfig.applicationListener(albHttpListener, {
        protocol: ApplicationProtocol.HTTP,
      }),
    });

    return service;
  }

  private createServiceIngressSecurityGroup({
    vpc,
    albIngressSecurityGroup,
  }: {
    vpc: IVpc;
    albIngressSecurityGroup: ISecurityGroup;
  }): ISecurityGroup {
    const EcsServiceIngressSecurityGroup: ISecurityGroup = new SecurityGroup(
      this,
      "EcsIngressSecurityGroup",
      {
        vpc,
      }
    );

    EcsServiceIngressSecurityGroup.addIngressRule(
      albIngressSecurityGroup,
      Port.allTraffic(),
      "Allow all inbound traffic from ALB"
    );

    EcsServiceIngressSecurityGroup.addIngressRule(
      EcsServiceIngressSecurityGroup,
      Port.allTraffic(),
      "Allow all inbound traffic from itself"
    );

    return EcsServiceIngressSecurityGroup;
  }

  private createTaskDefinition(imageTag: string): TaskDefinition {
    const taskDefinition: TaskDefinition = new FargateTaskDefinition(
      this,
      "TaskDefinition",
      {
        cpu: 256,
        memoryLimitMiB: 512,
      }
    );

    const ecrRepository: IRepository = Repository.fromRepositoryName(
      this,
      "ECRRepository",
      StringParameter.valueFromLookup(this, "huji-api-repository-name")
    );

    taskDefinition.addContainer(CONTAINER_NAME, {
      image: ContainerImage.fromEcrRepository(ecrRepository, imageTag),
      portMappings: [{ containerPort: 8080, protocol: Protocol.TCP }],
      logging: LogDrivers.awsLogs({
        streamPrefix: "api",
        logRetention: RetentionDays.TWO_WEEKS,
        datetimeFormat: "%Y-%m-%dT%H:%M:%S%z",
      }),
      secrets: {
        SPRING_DATASOURCE_USERNAME: EcsSecret.fromSecretsManager(
          this.postgresSecretTemplate,
          "username"
        ),
        SPRING_DATASOURCE_PASSWORD: EcsSecret.fromSecretsManager(
          this.postgresSecretTemplate,
          "password"
        ),
      },
      environment: {
        SPRING_DATASOURCE_URL: `jdbc:postgresql://${this.databaseInstance.instanceEndpoint.socketAddress}/${DATABASE_NAME}`,
      },
    });

    return taskDefinition;
  }

  private createPostgresDatabaseInstance({
    vpc,
    serviceIngressSecurityGroup,
    postgresSecretTemplate,
  }: {
    vpc: IVpc;
    serviceIngressSecurityGroup: ISecurityGroup;
    postgresSecretTemplate: Secret;
  }): IDatabaseInstance {
    const postgresIngressSecurityGroup: ISecurityGroup =
      this.createPostgresIngressSecurityGroup({
        vpc,
        serviceIngressSecurityGroup,
      });

    const postgresDatabaseInstance = new DatabaseInstance(
      this,
      "PostgresDatabaseInstance",
      {
        vpc,
        securityGroups: [postgresIngressSecurityGroup],
        vpcSubnets: vpc.selectSubnets({
          subnetGroupName: RDS_SUBNETS_GROUP_NAME,
        }),
        databaseName: DATABASE_NAME,
        engine: DatabaseInstanceEngine.postgres({
          version: PostgresEngineVersion.VER_16_3,
        }),
        instanceType: InstanceType.of(InstanceClass.T4G, InstanceSize.MICRO),
        allocatedStorage: 20,
        credentials: {
          username: postgresSecretTemplate
            .secretValueFromJson("username")
            .unsafeUnwrap(),
          password: postgresSecretTemplate.secretValueFromJson("password"),
        },
      }
    );

    return postgresDatabaseInstance;
  }

  private createPostgresIngressSecurityGroup({
    vpc,
    serviceIngressSecurityGroup,
  }: {
    vpc: IVpc;
    serviceIngressSecurityGroup: ISecurityGroup;
  }): ISecurityGroup {
    const postgresIngressSecurityGroup: ISecurityGroup = new SecurityGroup(
      this,
      "PostgresIngressSecurityGroup",
      {
        vpc,
      }
    );

    postgresIngressSecurityGroup.addIngressRule(
      serviceIngressSecurityGroup,
      Port.POSTGRES,
      "Allow inbound traffic from API"
    );

    return postgresIngressSecurityGroup;
  }

  private createTags(props: MainStackProps): void {
    Tags.of(this).add(APPLICATION_TAG_KEY, APPLICATION_TAG_VALUE);
    Tags.of(this).add(ENVIRONMENT_TAG_KEY, props.environmentName);
  }
}
