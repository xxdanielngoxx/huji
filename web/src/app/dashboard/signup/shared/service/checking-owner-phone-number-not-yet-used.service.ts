import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CheckingOwnerPhoneNumberNotYetUsedService {
  constructor(private httpClient: HttpClient) {}

  checkPhoneNumberNotYetUsed(phoneNumber: string): Observable<boolean> {
    return this.httpClient
      .get('/api/owners/checkPhoneNumberNotYetUsed', {
        params: {
          phone_number: phoneNumber,
        },
        observe: 'response',
      })
      .pipe(
        map(response => {
          return response.ok;
        }),
        catchError(() => {
          return of(false);
        })
      );
  }
}
