# Infrastructure project

The purpose of the infrastructure project is to manage infrastructure and deployment.

## Bootstrap CDK
Bootstrapping is the process of preparing your AWS environment for usage with the AWS Cloud Development Kit (AWS CDK). Before you deploy a CDK stack into an AWS environment, the environment must first be bootstrapped.

Access this [document](https://docs.aws.amazon.com/cdk/v1/guide/bootstrapping.html) to learn how to bootstrap the AWS CDK project.

## CDK

The `cdk.json` file tells the CDK Toolkit how to execute your app.

### Useful commands

* `npm run build`   compile typescript to js
* `npm run watch`   watch for changes and compile
* `npm run test`    perform the jest unit tests
* `npx cdk deploy`  deploy this stack to your default AWS account/region
* `npx cdk diff`    compare deployed stack with current state
* `npx cdk synth`   emits the synthesized CloudFormation template
