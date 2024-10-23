import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginWithUsernamePasswordRequest } from '../model/login-with-username-passsword-request.model';
import { AccessTokenResponse } from '../model/access-token-response.model';
import { Observable, tap } from 'rxjs';

export const ACCESS_TOKEN = 'ACCESS_TOKEN';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  constructor(private readonly httpClient: HttpClient) {}

  loginWithUsernamePassword(
    request: LoginWithUsernamePasswordRequest
  ): Observable<AccessTokenResponse> {
    return this.httpClient
      .post<AccessTokenResponse>('/api/v1/auth/login', request, {
        params: {
          authenticationMethod: 'usernamePassword',
        },
      })
      .pipe(tap(response => this.persistAccessToken(response.token)));
  }

  private persistAccessToken(accessToken: string): void {
    sessionStorage.setItem(ACCESS_TOKEN, accessToken);
  }
}
