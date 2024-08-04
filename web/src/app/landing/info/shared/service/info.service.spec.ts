import { TestBed } from '@angular/core/testing';

import { provideHttpClient } from '@angular/common/http';
import {
  HttpTestingController,
  provideHttpClientTesting,
  TestRequest,
} from '@angular/common/http/testing';

import { InfoService } from './info.service';
import { lastValueFrom } from 'rxjs';
import { InfoModel } from '../model/info.model';

describe('InfoService', () => {
  let service: InfoService;
  let httpTesting: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(InfoService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return info when fetch info successful', async () => {
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

    const infoPromise: Promise<InfoModel> = lastValueFrom(service.fetchInfo());

    const request: TestRequest = httpTesting.expectOne(
      {
        url: '/actuator/info',
        method: 'GET',
      },
      'Request to load application info'
    );
    request.flush(mockInfo);

    expect(await infoPromise).toEqual(mockInfo);

    httpTesting.verify();
  });
});
