import { computed, Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class LoadingSpinnerService {
  private _isShow = signal(false);

  public readonly isShow = computed(this._isShow);

  show(): void {
    this._isShow.set(true);
  }

  hide(): void {
    this._isShow.set(false);
  }
}
