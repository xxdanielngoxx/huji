import { TestBed } from '@angular/core/testing';

import { ACCESS_TOKEN, AuthenticationService } from './authentication.service';
import {
  HttpTestingController,
  provideHttpClientTesting,
  TestRequest,
} from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { AccessTokenResponse } from '../model/access-token-response.model';
import { LoginWithUsernamePasswordRequest } from '../model/login-with-username-passsword-request.model';
import { lastValueFrom } from 'rxjs';

describe('AuthenticationService', () => {
  let service: AuthenticationService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    sessionStorage.clear();

    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(AuthenticationService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    sessionStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('loginWithUsernamePasssword', () => {
    it('should return access token when authentication success', async () => {
      const request: LoginWithUsernamePasswordRequest = {
        username: 'test_username',
        password: 'test_password',
      };

      const loginWithUsernamePasswordPromise: Promise<AccessTokenResponse> =
        lastValueFrom(service.loginWithUsernamePassword(request));

      const httpTestRequest: TestRequest = httpTestingController.expectOne({
        url: '/api/v1/auth/login?authenticationMethod=usernamePassword',
        method: 'POST',
      });

      expect(httpTestRequest.request.body).toEqual(request);

      const mockAccessToken: AccessTokenResponse = {
        token: 'test',
      };
      httpTestRequest.flush(mockAccessToken, {
        status: 200,
        statusText: 'OK',
      });

      expect(await loginWithUsernamePasswordPromise).toEqual(mockAccessToken);

      expect(sessionStorage.getItem(ACCESS_TOKEN)).toEqual(
        mockAccessToken.token
      );
    });
  });
});
