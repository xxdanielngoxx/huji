import { ComponentFixture, TestBed } from '@angular/core/testing';

import {
  DefaultDialogComponent,
  DefaultDialogData,
} from './default-dialog.component';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

describe('DefaultDialogComponent', () => {
  let component: DefaultDialogComponent;
  let fixture: ComponentFixture<DefaultDialogComponent>;

  const dialogData: DefaultDialogData = {
    type: 'info',
    title: 'example title',
    content: 'example content',
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DefaultDialogComponent],
      providers: [
        {
          provide: MAT_DIALOG_DATA,
          useValue: dialogData,
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(DefaultDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
