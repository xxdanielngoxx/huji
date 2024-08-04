export interface InfoModel {
  git: GitInfoModel;
  build: BuildInfoModel;
}

export interface GitInfoModel {
  branch: string;
  commit: CommitInfoModel;
}

export interface CommitInfoModel {
  id: string;
  time: number;
}

export interface BuildInfoModel {
  artifact: string;
  name: string;
  time: number;
  version: string;
  group: string;
}
