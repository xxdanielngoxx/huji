import { TestBed } from '@angular/core/testing';
import { UniquePhoneNumberValidator } from './unique-phone-number-validator.directive';
import { CheckingOwnerPhoneNumberNotYetUsedService } from '../service/checking-owner-phone-number-not-yet-used.service';
import { FormControl, ValidationErrors } from '@angular/forms';
import { lastValueFrom, Observable, of } from 'rxjs';

describe('UniquePhoneNumberValidator', () => {
  let validator: UniquePhoneNumberValidator;
  const mockCheckingOwnerPhoneNumberDoesNotExistService = {
    checkPhoneNumberNotYetUsed: jest.fn(),
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {
          provide: CheckingOwnerPhoneNumberNotYetUsedService,
          useValue: mockCheckingOwnerPhoneNumberDoesNotExistService,
        },
      ],
    });

    validator = TestBed.inject(UniquePhoneNumberValidator);
  });

  it('should return uniquePhoneNumber error when phone number is already used by another owner', async () => {
    mockCheckingOwnerPhoneNumberDoesNotExistService.checkPhoneNumberNotYetUsed.mockReturnValue(
      of(false)
    );

    const phoneNumberControl = new FormControl('0393238017');
    const resultPromise: Promise<ValidationErrors | null> = lastValueFrom(
      validator.validate(
        phoneNumberControl
      ) as Observable<ValidationErrors | null>
    );

    expect(await resultPromise).toEqual({ uniquePhoneNumber: true });
  });

  it('should return null when phone number is not used by any owner', async () => {
    mockCheckingOwnerPhoneNumberDoesNotExistService.checkPhoneNumberNotYetUsed.mockReturnValue(
      of(true)
    );

    const phoneNumberControl = new FormControl('0393238017');
    const resultPromise: Promise<ValidationErrors | null> = lastValueFrom(
      validator.validate(
        phoneNumberControl
      ) as Observable<ValidationErrors | null>
    );

    expect(await resultPromise).toEqual(null);
  });
});
