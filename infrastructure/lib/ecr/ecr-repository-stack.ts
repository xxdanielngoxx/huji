import { Duration, RemovalPolicy, Stack, StackProps } from "aws-cdk-lib";
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
          description: `Delete untagged image after 3 days`,
          rulePriority: 1,
          tagStatus: TagStatus.UNTAGGED,
          maxImageAge: Duration.days(3),
        },
      ],
      imageTagMutability: TagMutability.MUTABLE,
    });
  }
}
