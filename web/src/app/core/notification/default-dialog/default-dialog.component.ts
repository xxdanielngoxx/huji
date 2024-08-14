import { Component, Inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle,
} from '@angular/material/dialog';

import { MatDividerModule } from '@angular/material/divider';
import {
  FontAwesomeModule,
  IconDefinition,
} from '@fortawesome/angular-fontawesome';
import {
  faCircleCheck,
  faCircleQuestion,
  faCircleXmark,
} from '@fortawesome/free-regular-svg-icons';
import { faTriangleExclamation } from '@fortawesome/free-solid-svg-icons';

export interface DefaultDialogData {
  type: DialogType;
  title: string;
  content: string;
  isDisplayCloseButton: boolean;
}

export type DialogType = 'success' | 'info' | 'warn' | 'error';

@Component({
  selector: 'app-default-dialog',
  standalone: true,
  imports: [
    MatDialogActions,
    MatDialogClose,
    MatDialogContent,
    MatDialogTitle,
    MatButtonModule,
    MatDividerModule,
    FontAwesomeModule,
  ],
  templateUrl: './default-dialog.component.html',
  styleUrl: './default-dialog.component.scss',
})
export class DefaultDialogComponent {
  icon: IconDefinition;
  constructor(@Inject(MAT_DIALOG_DATA) public data: DefaultDialogData) {
    this.icon = this.mapIconByType(data.type);
  }

  private mapIconByType(dialogType: DialogType): IconDefinition {
    switch (dialogType) {
      case 'success':
        return faCircleCheck;
      case 'info':
        return faCircleQuestion;
      case 'error':
        return faCircleXmark;
      case 'warn':
        return faTriangleExclamation;
    }
  }
}
