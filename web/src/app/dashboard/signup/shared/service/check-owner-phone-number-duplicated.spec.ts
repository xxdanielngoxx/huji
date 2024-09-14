import { TestBed } from '@angular/core/testing';

import { CheckOwnerPhoneNumberDuplicatedService } from './check-owner-phone-number-duplicated.service';
import { provideHttpClient } from '@angular/common/http';
import {
  HttpTestingController,
  provideHttpClientTesting,
  TestRequest,
} from '@angular/common/http/testing';
import { lastValueFrom } from 'rxjs';
import { CheckOwnerPhoneNumberDuplicatedResponse } from '../model/check-owner-phone-number-duplicated-response.model';

describe('CheckOwnerPhoneNumberDuplicatedService', () => {
  let service: CheckOwnerPhoneNumberDuplicatedService;
  let httpTesting: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(CheckOwnerPhoneNumberDuplicatedService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return result when receiving success response', async () => {
    const phoneNumber = '0393238017';
    const resultPromise: Promise<boolean> = lastValueFrom(
      service.checkPhoneNumberDuplicated(phoneNumber)
    );

    const testRequest: TestRequest = httpTesting.expectOne({
      method: 'POST',
      url: '/api/v1/users/actions/check-phone-number-duplicated',
    });
    expect(testRequest.request.body).toEqual({ phoneNumber });

    const mockResponse: CheckOwnerPhoneNumberDuplicatedResponse = {
      duplicated: true,
    };
    testRequest.flush(mockResponse, {
      status: 200,
      statusText: 'OK',
    });

    expect(await resultPromise).toEqual(mockResponse.duplicated);

    httpTesting.verify();
  });

  it('should return true when receiving error response', async () => {
    const phoneNumber = '0393238017';
    const resultPromise: Promise<boolean> = lastValueFrom(
      service.checkPhoneNumberDuplicated(phoneNumber)
    );

    const testRequest: TestRequest = httpTesting.expectOne({
      method: 'POST',
      url: '/api/v1/users/actions/check-phone-number-duplicated',
    });
    expect(testRequest.request.body).toEqual({ phoneNumber });

    testRequest.flush(null, {
      status: 500,
      statusText: 'Internal Server Error',
    });

    expect(await resultPromise).toEqual(true);

    httpTesting.verify();
  });
});
