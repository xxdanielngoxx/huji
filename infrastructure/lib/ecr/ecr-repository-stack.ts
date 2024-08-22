import { RemovalPolicy, Stack, StackProps } from "aws-cdk-lib";
import {
  IRepository,
  Repository,
  TagMutability,
  TagStatus,
} from "aws-cdk-lib/aws-ecr";
import { Construct } from "constructs";

export interface ECRRepositoryStackProps extends StackProps {
  readonly repositoryName: string;
}

export class ECRRepositoryStack extends Stack {
  private readonly ecrRepository: IRepository;

  constructor(scope?: Construct, id?: string, props?: ECRRepositoryStackProps) {
    super(scope, id, props);

    this.ecrRepository = new Repository(this, "ecrRepository", {
      repositoryName: props?.repositoryName,
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
}
