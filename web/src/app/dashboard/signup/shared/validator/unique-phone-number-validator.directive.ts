import { Injectable } from '@angular/core';
import {
  AbstractControl,
  AsyncValidator,
  ValidationErrors,
} from '@angular/forms';
import { map, Observable } from 'rxjs';
import { CheckingOwnerPhoneNumberNotYetUsedService } from '../service/checking-owner-phone-number-not-yet-used.service';

@Injectable({
  providedIn: 'root',
})
export class UniquePhoneNumberValidator implements AsyncValidator {
  constructor(
    private checkingOwnerPhoneNumberDoesNotExistService: CheckingOwnerPhoneNumberNotYetUsedService
  ) {}

  validate(
    control: AbstractControl
  ): Promise<ValidationErrors | null> | Observable<ValidationErrors | null> {
    return this.checkingOwnerPhoneNumberDoesNotExistService
      .checkPhoneNumberNotYetUsed(control.value)
      .pipe(
        map(notYetUsed => {
          if (notYetUsed) {
            return null;
          }

          return { uniquePhoneNumber: true };
        })
      );
  }
}
