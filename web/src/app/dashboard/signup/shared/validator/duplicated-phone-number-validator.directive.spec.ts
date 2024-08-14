import { TestBed } from '@angular/core/testing';
import { DuplicatedPhoneNumberValidator } from './duplicated-phone-number-validator.directive';
import { CheckOwnerPhoneNumberDuplicatedService } from '../service/check-owner-phone-number-duplicated.service';
import { FormControl, ValidationErrors } from '@angular/forms';
import { lastValueFrom, Observable, of } from 'rxjs';

describe('DuplicatedPhoneNumberValidator', () => {
  let validator: DuplicatedPhoneNumberValidator;
  const mockCheckOwnerPhoneNumberDuplicatedService = {
    checkPhoneNumberDuplicated: jest.fn(),
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {
          provide: CheckOwnerPhoneNumberDuplicatedService,
          useValue: mockCheckOwnerPhoneNumberDuplicatedService,
        },
      ],
    });

    validator = TestBed.inject(DuplicatedPhoneNumberValidator);
  });

  it('should return duplicatedPhoneNumber error when phone number is duplicated', async () => {
    mockCheckOwnerPhoneNumberDuplicatedService.checkPhoneNumberDuplicated.mockReturnValue(
      of(true)
    );

    const phoneNumberControl = new FormControl('0393238017');
    const resultPromise: Promise<ValidationErrors | null> = lastValueFrom(
      validator.validate(
        phoneNumberControl
      ) as Observable<ValidationErrors | null>
    );

    expect(await resultPromise).toEqual({ duplicatedPhoneNumber: true });
  });

  it('should return null when phone number is not duplicated', async () => {
    mockCheckOwnerPhoneNumberDuplicatedService.checkPhoneNumberDuplicated.mockReturnValue(
      of(false)
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
