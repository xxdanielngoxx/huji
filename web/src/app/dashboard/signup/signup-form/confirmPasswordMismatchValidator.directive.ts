import { AbstractControl, FormControl, ValidatorFn } from '@angular/forms';

export const confirmPasswordMismatchValidator = (
  password: FormControl
): ValidatorFn => {
  return (confirmPassword: AbstractControl) => {
    if (password.value === confirmPassword.value) {
      return null;
    }

    return {
      confirmPasswordMismatch: true,
    };
  };
};
