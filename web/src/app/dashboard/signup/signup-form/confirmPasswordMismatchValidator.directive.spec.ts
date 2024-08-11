import { FormControl } from '@angular/forms';
import { confirmPasswordMismatchValidator } from './confirmPasswordMismatchValidator.directive';

describe('confirmPasswordValidator', () => {
  it('should return error when password and confirmPassword mismatch', () => {
    const password = new FormControl('12345678');
    const confirmPassword = new FormControl('abcdedgh');

    const validator = confirmPasswordMismatchValidator(password);

    expect(validator(confirmPassword)).toEqual({
      confirmPasswordMismatch: true,
    });
  });

  it('should return null when password and confirmPassword are equal', () => {
    const password = new FormControl('12345678');
    const confirmPassword = new FormControl('12345678');

    const validator = confirmPasswordMismatchValidator(password);

    expect(validator(confirmPassword)).toEqual(null);
  });
});
