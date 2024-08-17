import { Injectable } from '@angular/core';
import {
  AbstractControl,
  AsyncValidator,
  ValidationErrors,
} from '@angular/forms';
import { map, Observable, of, switchMap, timer } from 'rxjs';
import { CheckOwnerPhoneNumberDuplicatedService } from '../service/check-owner-phone-number-duplicated.service';

@Injectable({
  providedIn: 'root',
})
export class DuplicatedPhoneNumberValidator implements AsyncValidator {
  constructor(
    private checkOwnerPhoneNumberDuplicatedService: CheckOwnerPhoneNumberDuplicatedService
  ) {}

  validate(
    control: AbstractControl
  ): Promise<ValidationErrors | null> | Observable<ValidationErrors | null> {
    if (!control.value) {
      return of(null);
    }

    return timer(300).pipe(
      switchMap(() =>
        this.checkOwnerPhoneNumberDuplicatedService
          .checkPhoneNumberDuplicated(control.value)
          .pipe(
            map(duplicated => {
              if (duplicated) {
                return { duplicatedPhoneNumber: true };
              }

              return null;
            })
          )
      )
    );
  }
}
