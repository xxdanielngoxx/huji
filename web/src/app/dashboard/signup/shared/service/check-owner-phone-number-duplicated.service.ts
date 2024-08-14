import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import { CheckOwnerPhoneNumberDuplicatedRequest } from '../model/check-owner-phone-number-duplicated-request.model';
import { CheckOwnerPhoneNumberDuplicatedResponse } from '../model/check-owner-phone-number-duplicated-response.model';

@Injectable({
  providedIn: 'root',
})
export class CheckOwnerPhoneNumberDuplicatedService {
  constructor(private httpClient: HttpClient) {}

  checkPhoneNumberDuplicated(phoneNumber: string): Observable<boolean> {
    const request: CheckOwnerPhoneNumberDuplicatedRequest = {
      phoneNumber,
    };
    return this.httpClient
      .post<CheckOwnerPhoneNumberDuplicatedResponse>(
        '/api/v1/owners/actions/check-phone-number-duplicated',
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
