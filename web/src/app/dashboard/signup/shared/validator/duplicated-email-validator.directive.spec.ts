import { TestBed } from '@angular/core/testing';
import { FormControl, ValidationErrors } from '@angular/forms';
import { lastValueFrom, Observable, of } from 'rxjs';
import { DuplicatedEmailValidator } from './duplicated-email-validator.directive';
import { CheckOwnerEmailDuplicatedService } from '../service/check-owner-email-duplicated.service';

describe('DuplicatedEmailValidator', () => {
  let validator: DuplicatedEmailValidator;
  const checkOwnerEmailDuplicatedService = {
    checkEmailDuplicated: jest.fn(),
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {
          provide: CheckOwnerEmailDuplicatedService,
          useValue: checkOwnerEmailDuplicatedService,
        },
      ],
    });

    validator = TestBed.inject(DuplicatedEmailValidator);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('should return duplicatedEmail error when email is duplicated', async () => {
    checkOwnerEmailDuplicatedService.checkEmailDuplicated.mockReturnValue(
      of(true)
    );

    const emailControl = new FormControl('danielngo1998@gmail.com');
    const resultPromise: Promise<ValidationErrors | null> = lastValueFrom(
      validator.validate(emailControl) as Observable<ValidationErrors | null>
    );

    expect(await resultPromise).toEqual({ duplicatedEmail: true });
    expect(
      checkOwnerEmailDuplicatedService.checkEmailDuplicated
    ).toHaveBeenCalledWith(emailControl.value);
  });

  it('should return null when email is not duplicated', async () => {
    checkOwnerEmailDuplicatedService.checkEmailDuplicated.mockReturnValue(
      of(false)
    );

    const emailControl = new FormControl('danielngo1998@gmail.com');
    const resultPromise: Promise<ValidationErrors | null> = lastValueFrom(
      validator.validate(emailControl) as Observable<ValidationErrors | null>
    );

    expect(await resultPromise).toEqual(null);
    expect(
      checkOwnerEmailDuplicatedService.checkEmailDuplicated
    ).toHaveBeenCalledWith(emailControl.value);
  });

  it('should return false and not call api to check duplicated email when control value is blank', async () => {
    checkOwnerEmailDuplicatedService.checkEmailDuplicated.mockReturnValue(
      of(false)
    );

    const emailControl = new FormControl('');
    const resultPromise: Promise<ValidationErrors | null> = lastValueFrom(
      validator.validate(emailControl) as Observable<ValidationErrors | null>
    );

    expect(await resultPromise).toEqual(null);
    expect(
      checkOwnerEmailDuplicatedService.checkEmailDuplicated
    ).toHaveBeenCalledTimes(0);
  });
});
