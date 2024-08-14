import { Injectable } from '@angular/core';
import {
  AbstractControl,
  AsyncValidator,
  ValidationErrors,
} from '@angular/forms';
import { map, Observable } from 'rxjs';
import { CheckOwnerEmailDuplicatedService } from '../service/check-owner-email-duplicated.service';

@Injectable({
  providedIn: 'root',
})
export class DuplicatedEmailValidator implements AsyncValidator {
  constructor(
    private checkOwnerEmailDuplicatedService: CheckOwnerEmailDuplicatedService
  ) {}

  validate(
    control: AbstractControl
  ): Promise<ValidationErrors | null> | Observable<ValidationErrors | null> {
    return this.checkOwnerEmailDuplicatedService
      .checkEmailDuplicated(control.value)
      .pipe(
        map(duplidated => {
          if (duplidated) {
            return { duplicatedEmail: true };
          }

          return null;
        })
      );
  }
}
