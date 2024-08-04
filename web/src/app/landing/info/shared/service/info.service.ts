import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { InfoModel } from '../model/info.model';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class InfoService {
  constructor(private httpClient: HttpClient) {}

  public fetchInfo(): Observable<InfoModel> {
    return this.httpClient.get<InfoModel>('/actuator/info');
  }
}
