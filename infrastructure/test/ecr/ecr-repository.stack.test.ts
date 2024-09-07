import { App } from "aws-cdk-lib";
import { ECRRepositoryStack } from "../../lib/ecr/ecr-repository.stack";
import { Match, Template } from "aws-cdk-lib/assertions";
import {
  APPLICATION_TAG_KEY,
  APPLICATION_TAG_VALUE,
} from "../../lib/constant/tag.constant";

describe("ECRRepositoryStack", () => {
  const app = new App();

  const ecrRepositoryStack = new ECRRepositoryStack(app, {
    projectName: "api",
  });

  const template = Template.fromStack(ecrRepositoryStack);
  test("should synthesized as expected", () => {
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
        RepositoryName: "com.github.xxdanielngoxx/huji/api",
        Tags: Match.arrayWith([
          {
            Key: APPLICATION_TAG_KEY,
            Value: APPLICATION_TAG_VALUE,
          },
        ]),
      })
    );
  });

  test("should store repository name to ssm parameter", () => {
    template.hasResourceProperties("AWS::SSM::Parameter", {
      Name: "huji-api-repository-name",
      Value: "com.github.xxdanielngoxx/huji/api",
    });
  });
});
