import { TestBed } from '@angular/core/testing';

import { CheckingOwnerPhoneNumberNotYetUsedService } from './checking-owner-phone-number-not-yet-used.service';
import { provideHttpClient } from '@angular/common/http';
import {
  HttpTestingController,
  provideHttpClientTesting,
  TestRequest,
} from '@angular/common/http/testing';
import { lastValueFrom } from 'rxjs';

describe('CheckingOwnerPhoneNumberNotYetUsedService', () => {
  let service: CheckingOwnerPhoneNumberNotYetUsedService;
  let httpTesting: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(CheckingOwnerPhoneNumberNotYetUsedService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return true when phone number is not used by any owner', async () => {
    const phoneNumber = '0393238017';
    const resultPromise: Promise<boolean> = lastValueFrom(
      service.checkPhoneNumberNotYetUsed(phoneNumber)
    );

    const testRequest: TestRequest = httpTesting.expectOne(
      req =>
        req.method === 'GET' &&
        req.url === '/api/owners/checkPhoneNumberNotYetUsed'
    );
    expect(testRequest.request.params.get('phone_number')).toEqual(phoneNumber);
    testRequest.flush(null, {
      status: 200,
      statusText: 'OK',
    });

    expect(await resultPromise).toEqual(true);

    httpTesting.verify();
  });

  it('should return false when phone number is used by another owner', async () => {
    const phoneNumber = '0393238017';
    const resultPromise: Promise<boolean> = lastValueFrom(
      service.checkPhoneNumberNotYetUsed(phoneNumber)
    );

    const testRequest: TestRequest = httpTesting.expectOne(
      req =>
        req.method === 'GET' &&
        req.url === '/api/owners/checkPhoneNumberNotYetUsed'
    );
    expect(testRequest.request.params.get('phone_number')).toEqual(phoneNumber);
    testRequest.flush(null, {
      status: 400,
      statusText: 'Bad Request',
    });

    expect(await resultPromise).toEqual(false);

    httpTesting.verify();
  });
});
