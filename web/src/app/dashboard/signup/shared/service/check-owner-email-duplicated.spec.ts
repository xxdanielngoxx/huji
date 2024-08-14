import { TestBed } from '@angular/core/testing';

import { provideHttpClient } from '@angular/common/http';
import {
  HttpTestingController,
  provideHttpClientTesting,
  TestRequest,
} from '@angular/common/http/testing';
import { lastValueFrom } from 'rxjs';
import { CheckOwnerEmailDuplicatedService } from './check-owner-email-duplicated.service';
import { CheckOwnerPhoneNumberDuplicatedResponse } from '../model/check-owner-phone-number-duplicated-response.model';

describe('CheckOwnerEmailDuplicatedService', () => {
  let service: CheckOwnerEmailDuplicatedService;
  let httpTesting: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(CheckOwnerEmailDuplicatedService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return result when receiving success response', async () => {
    const email = 'danielngo1998@gmail.com';

    const resultPromise: Promise<boolean> = lastValueFrom(
      service.checkEmailDuplicated(email)
    );

    const testRequest: TestRequest = httpTesting.expectOne({
      method: 'POST',
      url: '/api/v1/owners/actions/check-email-duplicated',
    });
    expect(testRequest.request.body).toEqual({ email });

    const mockResponse: CheckOwnerPhoneNumberDuplicatedResponse = {
      duplicated: true,
    };
    testRequest.flush(mockResponse, {
      status: 200,
      statusText: 'OK',
    });

    expect(await resultPromise).toEqual(mockResponse.duplicated);
  });

  it('should return true when receving error response', async () => {
    const email = 'danielngo1998@gmail.com';

    const resultPromise: Promise<boolean> = lastValueFrom(
      service.checkEmailDuplicated(email)
    );

    const testRequest: TestRequest = httpTesting.expectOne({
      method: 'POST',
      url: '/api/v1/owners/actions/check-email-duplicated',
    });
    expect(testRequest.request.body).toEqual({ email });

    testRequest.flush(null, {
      status: 500,
      statusText: 'Internal Server Error',
    });

    expect(await resultPromise).toEqual(true);
  });
});
