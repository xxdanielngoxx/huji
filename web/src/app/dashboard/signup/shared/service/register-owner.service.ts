import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegisterOwnerRequest } from '../model/register-owner-request.model';
import { map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class RegisterOwnerService {
  constructor(private httpClient: HttpClient) {}

  registerOwner(request: RegisterOwnerRequest): Observable<string> {
    return this.httpClient
      .post('/api/v1/owners', request, { observe: 'response' })
      .pipe(
        map(response => {
          const location = response.headers.get('Location');

          if (!location) {
            throw new Error("Expect to receive a non-null 'Location' header");
          }

          return location;
        })
      );
  }
}
