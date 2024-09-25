import { App } from "aws-cdk-lib";
import { MainStack } from "../../lib/main/main.stack";
import { Match, Template } from "aws-cdk-lib/assertions";
import {
  APPLICATION_TAG_KEY,
  APPLICATION_TAG_VALUE,
  ENVIRONMENT_TAG_KEY,
} from "../../lib/constant/tag.constant";

describe("MainStack", () => {
  describe("Vpc", () => {
    test("should create VPC with 2 public subnet, 2 private subnet, 1 IGW and 1 NAT Gateway", () => {
      const app = new App();

      const mainStack = new MainStack(app, {
        environmentName: "dev",
        imageTag: "1.0.0",
        env: {
          account: "209479288946",
          region: "ap-southeast-1",
        },
      });

      const template = Template.fromStack(mainStack);

      template.resourceCountIs("AWS::EC2::InternetGateway", 1);
      template.resourceCountIs("AWS::EC2::NatGateway", 1);
      template.resourceCountIs("AWS::EC2::Subnet", 6);

      for (let i = 0; i < 4; i++) {
        template.hasResourceProperties("AWS::EC2::Subnet", {
          CidrBlock: `10.0.${i}.0/24`,
        });
      }

      for (let i = 0; i < 2; i++) {
        template.hasResourceProperties("AWS::EC2::Subnet", {
          CidrBlock: `10.0.4.${i * 16}/28`,
        });
      }
    });

    test("should create environment tag, application tag", () => {
      const app = new App();

      const mainStack = new MainStack(app, {
        environmentName: "dev",
        imageTag: "1.0.0",
        env: {
          account: "209479288946",
          region: "ap-southeast-1",
        },
      });

      const template = Template.fromStack(mainStack);

      template.hasResourceProperties("AWS::EC2::VPC", {
        Tags: Match.arrayWith([
          {
            Key: APPLICATION_TAG_KEY,
            Value: APPLICATION_TAG_VALUE,
          },
          {
            Key: ENVIRONMENT_TAG_KEY,
            Value: "dev",
          },
        ]),
      });
    });
  });

  describe("Application Load Balancer", () => {
    const app = new App();

    const mainStack = new MainStack(app, {
      environmentName: "dev",
      imageTag: "1.0.0",
      env: {
        account: "209479288946",
        region: "ap-southeast-1",
      },
    });

    const template = Template.fromStack(mainStack);

    test("should create ingress security group for ALB", () => {
      template.hasResourceProperties("AWS::EC2::SecurityGroup", {
        SecurityGroupEgress: [
          {
            CidrIp: "0.0.0.0/0",
            Description: "Allow all outbound traffic by default",
            IpProtocol: "-1",
          },
        ],
        SecurityGroupIngress: [
          {
            CidrIp: "0.0.0.0/0",
            Description: "Allow all inbound ipv4 HTTP traffic",
            FromPort: 80,
            IpProtocol: "tcp",
            ToPort: 80,
          },
          {
            CidrIp: "0.0.0.0/0",
            Description: "Allow all inbound ipv4 HTTPS traffic",
            FromPort: 443,
            IpProtocol: "tcp",
            ToPort: 443,
          },
        ],
        GroupDescription: "MainStack/AlbIngressSecurityGroup",
      });
    });

    test("should create ALB", () => {
      template.hasResourceProperties(
        "AWS::ElasticLoadBalancingV2::LoadBalancer",
        {
          Scheme: "internet-facing",
          Type: "application",
          SecurityGroups: [
            {
              "Fn::GetAtt": ["AlbIngressSecurityGroupD980EDC6", "GroupId"],
            },
          ],
          Subnets: [
            { Ref: "VpcIngressSubnet1SubnetBB860652" },
            { Ref: "VpcIngressSubnet2Subnet0EF3AA18" },
          ],
        }
      );
    });
  });

  describe("ECS", () => {
    const app = new App();

    const mainStack = new MainStack(app, {
      environmentName: "dev",
      imageTag: "1.0.0",
      env: {
        account: "209479288946",
        region: "ap-southeast-1",
      },
    });

    const template = Template.fromStack(mainStack);

    describe("EcsCluster", () => {
      test("should create cluster", () => {
        template.resourceCountIs("AWS::ECS::Cluster", 1);
      });
    });

    describe("FargateService", () => {
      const app = new App();

      const imageTag = "1.0.0";
      const mainStack = new MainStack(app, {
        environmentName: "dev",
        imageTag,
        env: {
          account: "209479288946",
          region: "ap-southeast-1",
        },
      });

      const template = Template.fromStack(mainStack);

      test("TaskDefinition", () => {
        template.hasResourceProperties("AWS::ECS::TaskDefinition", {
          ContainerDefinitions: [
            {
              Essential: true,
              Image: {
                "Fn::Join": [
                  "",
                  [
                    "209479288946.dkr.ecr.ap-southeast-1.",
                    {
                      Ref: "AWS::URLSuffix",
                    },
                    `/dummy-value-for-huji-api-repository-name:1.0.0`,
                  ],
                ],
              },
              LogConfiguration: {
                LogDriver: "awslogs",
                Options: {
                  "awslogs-group": {
                    Ref: "TaskDefinitionApiContainerLogGroup3C54E537",
                  },
                  "awslogs-stream-prefix": "api",
                  "awslogs-region": "ap-southeast-1",
                  "awslogs-datetime-format": "%Y-%m-%dT%H:%M:%S%z",
                },
              },
              Name: "ApiContainer",
              PortMappings: [
                {
                  ContainerPort: 8080,
                  Protocol: "tcp",
                },
              ],
              Environment: [
                {
                  Name: "SPRING_DATASOURCE_URL",
                  Value: {
                    "Fn::Join": [
                      "",
                      [
                        "jdbc:postgresql://",
                        {
                          "Fn::GetAtt": [
                            "PostgresDatabaseInstance0FFC3281",
                            "Endpoint.Address",
                          ],
                        },
                        ":",
                        {
                          "Fn::GetAtt": [
                            "PostgresDatabaseInstance0FFC3281",
                            "Endpoint.Port",
                          ],
                        },
                        "/huji",
                      ],
                    ],
                  },
                },
              ],
              Secrets: [
                {
                  Name: "SPRING_DATASOURCE_USERNAME",
                  ValueFrom: {
                    "Fn::Join": [
                      "",
                      [
                        {
                          Ref: "PostgresSecretTemplateBB72B9A9",
                        },
                        ":username::",
                      ],
                    ],
                  },
                },
                {
                  Name: "SPRING_DATASOURCE_PASSWORD",
                  ValueFrom: {
                    "Fn::Join": [
                      "",
                      [
                        {
                          Ref: "PostgresSecretTemplateBB72B9A9",
                        },
                        ":password::",
                      ],
                    ],
                  },
                },
                {
                  "Name": "HUJI_SECURITY_JWT_SIGNING_KEY",
                  "ValueFrom": {
                   "Ref": "JwtSigningKeySecret3ECB8B41"
                  }
                 }
              ],
            },
          ],
          Cpu: "256",
          Memory: "512",
          NetworkMode: "awsvpc",
          RequiresCompatibilities: ["FARGATE"],
        });
      });

      test("EcsServiceSecurityGroup", () => {
        template.hasResourceProperties("AWS::EC2::SecurityGroup", {
          GroupDescription: "MainStack/EcsIngressSecurityGroup",
          SecurityGroupEgress: [
            {
              CidrIp: "0.0.0.0/0",
              Description: "Allow all outbound traffic by default",
              IpProtocol: "-1",
            },
          ],
        });

        template.hasResourceProperties("AWS::EC2::SecurityGroupIngress", {
          Description: "Allow all inbound traffic from ALB",
          GroupId: {
            "Fn::GetAtt": ["EcsIngressSecurityGroupBA884439", "GroupId"],
          },
          IpProtocol: "-1",
          SourceSecurityGroupId: {
            "Fn::GetAtt": ["AlbIngressSecurityGroupD980EDC6", "GroupId"],
          },
        });

        template.hasResourceProperties("AWS::EC2::SecurityGroupIngress", {
          Description: "Allow all inbound traffic from itself",
          GroupId: {
            "Fn::GetAtt": ["EcsIngressSecurityGroupBA884439", "GroupId"],
          },
          IpProtocol: "-1",
          SourceSecurityGroupId: {
            "Fn::GetAtt": ["EcsIngressSecurityGroupBA884439", "GroupId"],
          },
        });
      });

      test("should create fargate service", () => {
        template.hasResourceProperties("AWS::ECS::Service", {
          Cluster: {
            Ref: "EcsCluster97242B84",
          },
          LaunchType: "FARGATE",
          LoadBalancers: [
            {
              ContainerName: "ApiContainer",
              ContainerPort: 8080,
              TargetGroupArn: {
                Ref: "AlbHttpListenerECSGroup2AC28738",
              },
            },
          ],
          NetworkConfiguration: {
            AwsvpcConfiguration: {
              AssignPublicIp: "DISABLED",
              SecurityGroups: [
                {
                  "Fn::GetAtt": ["EcsIngressSecurityGroupBA884439", "GroupId"],
                },
              ],
              Subnets: [
                {
                  Ref: "VpcEcsClusterSubnet1Subnet111BC143",
                },
                {
                  Ref: "VpcEcsClusterSubnet2Subnet5975F0F8",
                },
              ],
            },
          },
          TaskDefinition: {
            Ref: "TaskDefinitionB36D86D9",
          },
        });
      });

      test("should create ecs target group", () => {
        template.hasResourceProperties(
          "AWS::ElasticLoadBalancingV2::TargetGroup",
          {
            Port: 80,
            Protocol: "HTTP",
            TargetType: "ip",
            VpcId: {
              Ref: "Vpc8378EB38",
            },
          }
        );
      });

      test("should create alb http listener", () => {
        template.hasResourceProperties(
          "AWS::ElasticLoadBalancingV2::Listener",
          {
            DefaultActions: [
              {
                TargetGroupArn: {
                  Ref: "AlbHttpListenerECSGroup2AC28738",
                },
                Type: "forward",
              },
            ],
            LoadBalancerArn: {
              Ref: "Alb16C2F182",
            },
            Port: 80,
            Protocol: "HTTP",
          }
        );
      });
    });
  });

  describe("Rds", () => {
    const app = new App();

    const mainStack = new MainStack(app, {
      environmentName: "dev",
      imageTag: "1.0.0",
      env: {
        account: "209479288946",
        region: "ap-southeast-1",
      },
    });

    const template = Template.fromStack(mainStack);

    describe("PostgresIngressSecurityGroup", () => {
      test("should create security group for rds", () => {
        template.hasResourceProperties("AWS::EC2::SecurityGroup", {
          GroupDescription: "MainStack/PostgresIngressSecurityGroup",
          SecurityGroupEgress: [
            {
              CidrIp: "0.0.0.0/0",
              Description: "Allow all outbound traffic by default",
              IpProtocol: "-1",
            },
          ],
          VpcId: {
            Ref: "Vpc8378EB38",
          },
        });
      });

      test("should allow inbound traffic from ecs cluster", () => {
        template.hasResourceProperties("AWS::EC2::SecurityGroupIngress", {
          Description: "Allow inbound traffic from API",
          FromPort: 5432,
          GroupId: {
            "Fn::GetAtt": ["PostgresIngressSecurityGroup8D68ECD8", "GroupId"],
          },
          IpProtocol: "tcp",
          SourceSecurityGroupId: {
            "Fn::GetAtt": ["EcsIngressSecurityGroupBA884439", "GroupId"],
          },
          ToPort: 5432,
        });
      });
    });

    describe("PostgresDatabaseInstance", () => {
      test("should create postgres database instance", () => {
        template.resourceCountIs("AWS::RDS::DBInstance", 1);

        template.hasResourceProperties("AWS::RDS::DBInstance", {
          AllocatedStorage: "20",
          CopyTagsToSnapshot: true,
          DBInstanceClass: "db.t4g.micro",
          DBName: "huji",
          DBSubnetGroupName: {
            Ref: "PostgresDatabaseInstanceSubnetGroup703BD80C",
          },
          Engine: "postgres",
          EngineVersion: "16.3",
          MasterUserPassword: {
            "Fn::Join": [
              "",
              [
                "{{resolve:secretsmanager:",
                {
                  Ref: "PostgresSecretTemplateBB72B9A9",
                },
                ":SecretString:password::}}",
              ],
            ],
          },
          MasterUsername: {
            "Fn::Join": [
              "",
              [
                "{{resolve:secretsmanager:",
                {
                  Ref: "PostgresSecretTemplateBB72B9A9",
                },
                ":SecretString:username::}}",
              ],
            ],
          },
          PubliclyAccessible: false,
          StorageType: "gp2",
          VPCSecurityGroups: [
            {
              "Fn::GetAtt": ["PostgresIngressSecurityGroup8D68ECD8", "GroupId"],
            },
          ],
        });
      });
    });
  });
});
