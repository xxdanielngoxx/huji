import { TestBed } from '@angular/core/testing';

import { provideHttpClient } from '@angular/common/http';
import {
  HttpTestingController,
  provideHttpClientTesting,
  TestRequest,
} from '@angular/common/http/testing';

import { RegisterOwnerService } from './register-owner.service';
import { lastValueFrom } from 'rxjs';
import { RegisterOwnerRequest } from '../model/register-owner-request.model';

describe('RegisterOwnerService', () => {
  let service: RegisterOwnerService;
  let httpTesting: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(RegisterOwnerService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it("should return the 'Location' represent registered owner resource", async () => {
    const registerOwnerRequest: RegisterOwnerRequest = {
      fullName: 'Ngô Đình Lộc',
      phoneNumber: '0393238017',
      password: 'super_secret?#',
      email: 'danielngo1998@gmail.com',
    };

    const registerOwnerResultPromise: Promise<string> = lastValueFrom(
      service.registerOwner(registerOwnerRequest)
    );

    const httpTestRequest: TestRequest = httpTesting.expectOne({
      url: '/api/owners',
      method: 'POST',
    });
    expect(httpTestRequest.request.body).toEqual(registerOwnerRequest);

    const mockLocationHeader =
      '/api/owners/de780ca6-dfe7-43ef-ac02-042a0f1a03eb';
    httpTestRequest.flush(null, {
      headers: {
        Location: mockLocationHeader,
      },
      status: 201,
      statusText: 'Created',
    });

    expect(await registerOwnerResultPromise).toEqual(mockLocationHeader);

    httpTesting.verify();
  });

  it("should throw error when 'Location' header is not present", async () => {
    const registerOwnerRequest: RegisterOwnerRequest = {
      fullName: 'Ngô Đình Lộc',
      phoneNumber: '0393238017',
      password: 'super_secret?#',
      email: 'danielngo1998@gmail.com',
    };

    const registerOwnerResultPromise: Promise<string> = lastValueFrom(
      service.registerOwner(registerOwnerRequest)
    );

    const httpTestRequest: TestRequest = httpTesting.expectOne({
      url: '/api/owners',
      method: 'POST',
    });
    expect(httpTestRequest.request.body).toEqual(registerOwnerRequest);

    httpTestRequest.flush(null, {
      status: 201,
      statusText: 'Created',
    });

    try {
      await registerOwnerResultPromise;
    } catch (error) {
      expect(error).toEqual(
        new Error("Expect to receive a non-null 'Location' header")
      );
    }

    httpTesting.verify();
  });
});
