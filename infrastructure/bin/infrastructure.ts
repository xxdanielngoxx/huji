#!/usr/bin/env node
import "source-map-support/register";
import * as cdk from "aws-cdk-lib";
import { ECRRepositoryStack } from "../lib/ecr/ecr-repository.stack";

const app = new cdk.App();

const env: cdk.Environment = {
  account: app.node.getContext("accountId"),
  region: app.node.getContext("region"),
};

const ecrRepositoryStack = new ECRRepositoryStack(app, {
  repositoryName: `com.github.xxdanielngoxx/hui/${app.node.getContext(
    "projectName"
  )}`,
  env,
});
