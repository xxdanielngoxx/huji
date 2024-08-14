import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import { CheckOwnerEmailDuplicatedRequest } from '../model/check-owner-email-duplicated-request-model';
import { CheckOwnerEmailDuplicatedResponse } from '../model/check-owner-email-duplicated-response.model';

@Injectable({
  providedIn: 'root',
})
export class CheckOwnerEmailDuplicatedService {
  constructor(private httpClient: HttpClient) {}

  checkEmailDuplicated(email: string): Observable<boolean> {
    const request: CheckOwnerEmailDuplicatedRequest = {
      email,
    };
    return this.httpClient
      .post<CheckOwnerEmailDuplicatedResponse>(
        '/api/v1/owners/actions/check-email-duplicated',
        request
      )
      .pipe(
        map(response => {
          return response.duplicated;
        }),
        catchError(() => {
          return of(true);
        })
      );
  }
}
