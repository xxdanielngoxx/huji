import { Component, computed, inject, Signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';

import { MatCardModule } from '@angular/material/card';
import { ItemComponent } from './item/item.component';
import { InfoService } from './shared/service/info.service';
import {
  BuildInfoModel,
  CommitInfoModel,
  InfoModel,
} from './shared/model/info.model';
import { IconDefinition } from '@fortawesome/angular-fontawesome';
import { faClock, faPaperPlane } from '@fortawesome/free-solid-svg-icons';
import { faSquareGit } from '@fortawesome/free-brands-svg-icons';

export interface ComponentInfoModel {
  commit: ComponentInfoCommitModel;
  build: ComponentInfoBuildModel;
}

export interface ComponentInfoCommitModel {
  commitId: ComponentInfoItemModel;
  commitAt: ComponentInfoItemModel;
}

export interface ComponentInfoBuildModel {
  version: ComponentInfoItemModel;
  buildAt: ComponentInfoItemModel;
}

export interface ComponentInfoItemModel {
  propertyName: string;
  value: string;
  icon: IconDefinition;
}

@Component({
  selector: 'app-info',
  standalone: true,
  imports: [MatCardModule, InfoComponent, ItemComponent],
  templateUrl: './info.component.html',
  styleUrl: './info.component.scss',
})
export class InfoComponent {
  private infoService: InfoService = inject(InfoService);

  fetchedInfo: Signal<InfoModel | undefined> = toSignal(
    this.infoService.fetchInfo()
  );

  info: Signal<ComponentInfoModel | undefined> = computed(() => {
    const info: InfoModel | undefined = this.fetchedInfo();

    if (!info) {
      return undefined;
    }

    return {
      build: this.extractBuildInfo(info.build),
      commit: this.extractCommitInfo(info.git.commit),
    };
  });

  private extractBuildInfo(
    fetchedBuildInfo: BuildInfoModel
  ): ComponentInfoBuildModel {
    const versionInfo: ComponentInfoItemModel = {
      propertyName: 'Phiên bản',
      value: fetchedBuildInfo.version,
      icon: faPaperPlane,
    };

    const buildAtInfo: ComponentInfoItemModel = {
      propertyName: 'Thời điểm phát hành',
      value: new Date(fetchedBuildInfo.time).toISOString(),
      icon: faClock,
    };

    return {
      version: versionInfo,
      buildAt: buildAtInfo,
    };
  }

  private extractCommitInfo(
    fetchedCommitInfo: CommitInfoModel
  ): ComponentInfoCommitModel {
    const commitIdInfo: ComponentInfoItemModel = {
      propertyName: 'Git commit id',
      value: fetchedCommitInfo.id,
      icon: faSquareGit,
    };

    const commitAtInfo: ComponentInfoItemModel = {
      propertyName: 'Thời điểm commit',
      value: new Date(fetchedCommitInfo.time).toISOString(),
      icon: faClock,
    };

    return {
      commitId: commitIdInfo,
      commitAt: commitAtInfo,
    };
  }
}
