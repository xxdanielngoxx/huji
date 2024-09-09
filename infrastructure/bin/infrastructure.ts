#!/usr/bin/env node
import "source-map-support/register";
import * as cdk from "aws-cdk-lib";
import { ECRRepositoryStack } from "../lib/ecr/ecr-repository.stack";
import { MainStack } from "../lib/main/main.stack";

const app = new cdk.App();

const env: cdk.Environment = {
  account: app.node.getContext("accountId"),
  region: app.node.getContext("region"),
};

const ecrRepositoryStack = new ECRRepositoryStack(app, {
  projectName: app.node.tryGetContext("projectName"),
  env,
});

const mainStack = new MainStack(app, {
  environmentName: app.node.tryGetContext("environmentName"),
  imageTag: app.node.tryGetContext("imageTag"),
  env,
});
