import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InfoComponent } from './info.component';
import { InfoService } from './shared/service/info.service';
import { InfoModel } from './shared/model/info.model';
import { of } from 'rxjs';

describe('InfoComponent', () => {
  let component: InfoComponent;
  let fixture: ComponentFixture<InfoComponent>;

  const mockInfo: InfoModel = {
    git: {
      branch: 'feature/setup-project',
      commit: {
        id: '6de063d',
        time: 1722136665,
      },
    },
    build: {
      artifact: 'api',
      name: 'api',
      time: 1722558828.685,
      version: '0.0.1-SNAPSHOT',
      group: 'com.github.xxdanielngoxx.hui',
    },
  };
  const mockInfoService = {
    fetchInfo: jest.fn(() => of(mockInfo)),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InfoComponent],
      providers: [
        {
          provide: InfoService,
          useValue: mockInfoService,
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(InfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
