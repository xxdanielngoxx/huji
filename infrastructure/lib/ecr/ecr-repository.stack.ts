import { RemovalPolicy, Stack, StackProps, Tags } from "aws-cdk-lib";
import {
  IRepository,
  Repository,
  TagMutability,
  TagStatus,
} from "aws-cdk-lib/aws-ecr";
import { Construct } from "constructs";
import {
  APPLICATION_TAG_KEY,
  APPLICATION_TAG_VALUE,
} from "../constant/tag.constant";
import { StringParameter } from "aws-cdk-lib/aws-ssm";

export interface ECRRepositoryStackProps extends StackProps {
  readonly projectName: string;
}

export class ECRRepositoryStack extends Stack {
  private readonly ecrRepository: IRepository;

  constructor(scope: Construct, props: ECRRepositoryStackProps) {
    super(scope, "ECRRepositoryStack", props);

    const repositoryName = `com.github.xxdanielngoxx/huji/${props.projectName}`;

    this.ecrRepository = this.createEcrRepository({ repositoryName });

    this.exportEcrRepositoryParameter({
      repositoryName,
      projectName: props.projectName,
    });

    this.createTags();
  }

  private createEcrRepository(props: { repositoryName: string }): IRepository {
    return new Repository(this, "ecrRepository", {
      repositoryName: props.repositoryName,
      removalPolicy: RemovalPolicy.DESTROY,
      lifecycleRules: [
        {
          description: `Retain maximum 25 images`,
          rulePriority: 1,
          tagStatus: TagStatus.ANY,
          maxImageCount: 25,
        },
      ],
      imageTagMutability: TagMutability.IMMUTABLE,
    });
  }

  private exportEcrRepositoryParameter(props: {
    repositoryName: string;
    projectName: string;
  }): void {
    new StringParameter(this, "RepositoryName", {
      stringValue: props.repositoryName,
      parameterName: `huji-${props.projectName}-repository-name`,
    });
  }

  private createTags(): void {
    Tags.of(this).add(APPLICATION_TAG_KEY, APPLICATION_TAG_VALUE);
  }
}
