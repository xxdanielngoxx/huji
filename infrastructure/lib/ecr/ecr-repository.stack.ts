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

export interface ECRRepositoryStackProps extends StackProps {
  readonly repositoryName: string;
}

export class ECRRepositoryStack extends Stack {
  private readonly ecrRepository: IRepository;

  constructor(scope: Construct, props: ECRRepositoryStackProps) {
    super(scope, "ECRRepositoryStack", props);

    this.ecrRepository = this.createEcrRepository(props);

    this.createTags();
  }

  private createEcrRepository(props: ECRRepositoryStackProps): IRepository {
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

  private createTags(): void {
    Tags.of(this).add(APPLICATION_TAG_KEY, APPLICATION_TAG_VALUE);
  }
}