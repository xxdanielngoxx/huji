import { TestBed } from '@angular/core/testing';
import {
  HttpClient,
  HttpInterceptorFn,
  provideHttpClient,
  withInterceptors,
} from '@angular/common/http';

import { loadingSpinnerInterceptor } from './loading-spinner.interceptor';
import {
  HttpTestingController,
  provideHttpClientTesting,
} from '@angular/common/http/testing';
import { LoadingSpinnerService } from '../service/loading-spinner.service';

import { jest, expect } from '@jest/globals';

describe('loadingSpinnerInterceptor', () => {
  let httpTesting: HttpTestingController;
  let httpClient: HttpClient;
  let loadingSpinnerService: LoadingSpinnerService;

  const interceptor: HttpInterceptorFn = (req, next) =>
    TestBed.runInInjectionContext(() => loadingSpinnerInterceptor(req, next));

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([loadingSpinnerInterceptor])),
        provideHttpClientTesting(),
        LoadingSpinnerService,
      ],
    });

    httpTesting = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
    loadingSpinnerService = TestBed.inject(LoadingSpinnerService);
  });

  it('should be created', () => {
    expect(interceptor).toBeTruthy();
  });

  it('should show loading spinner when calling API', () => {
    const spiedShow = jest.spyOn(loadingSpinnerService, 'show');
    const spiedHide = jest.spyOn(loadingSpinnerService, 'hide');

    httpClient.get('/api/test1').subscribe();
    httpClient.get('/actuator/test2').subscribe();
    httpClient.get('/other/test3').subscribe();

    const request1 = httpTesting.expectOne({
      url: '/api/test1',
    });

    const request2 = httpTesting.expectOne({
      url: '/actuator/test2',
    });

    const request3 = httpTesting.expectOne({
      url: '/other/test3',
    });

    request1.flush('');
    request2.flush('', {
      status: 500,
      statusText: 'Internal Server Error',
    });
    request3.flush('');

    expect(spiedShow).toHaveBeenCalledTimes(2);
    expect(spiedHide).toHaveBeenCalledTimes(2);
    expect(loadingSpinnerService.isShow()).toEqual(false);

    httpTesting.verify();
  });
});
