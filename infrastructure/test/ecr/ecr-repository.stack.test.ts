import { App } from "aws-cdk-lib";
import { ECRRepositoryStack } from "../../lib/ecr/ecr-repository.stack";
import { Match, Template } from "aws-cdk-lib/assertions";
import {
  APPLICATION_TAG_KEY,
  APPLICATION_TAG_VALUE,
} from "../../lib/constant/tag.constant";

describe("ECRRepositoryStack", () => {
  test("should synthesized as expected", () => {
    const app = new App();

    const ecrRepositoryStack = new ECRRepositoryStack(app, {
      repositoryName: "com.github.xxdanielngoxx/hui/api",
    });

    const template = Template.fromStack(ecrRepositoryStack);

    template.hasResourceProperties(
      "AWS::ECR::Repository",
      Match.objectEquals({
        ImageTagMutability: "IMMUTABLE",
        LifecyclePolicy: {
          LifecyclePolicyText: Match.serializedJson({
            rules: [
              {
                rulePriority: 1,
                description: "Retain maximum 25 images",
                selection: {
                  tagStatus: "any",
                  countType: "imageCountMoreThan",
                  countNumber: 25,
                },
                action: { type: "expire" },
              },
            ],
          }),
        },
        RepositoryName: "com.github.xxdanielngoxx/hui/api",
        Tags: Match.arrayWith([
          {
            Key: APPLICATION_TAG_KEY,
            Value: APPLICATION_TAG_VALUE,
          },
        ]),
      })
    );
  });
});
