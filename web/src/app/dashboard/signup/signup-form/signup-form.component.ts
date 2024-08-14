import { Component, DestroyRef, inject, signal } from '@angular/core';
import {
  ReactiveFormsModule,
  Validators,
  FormGroup,
  FormControl,
} from '@angular/forms';
import { Router } from '@angular/router';

import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { confirmPasswordMismatchValidator } from './confirmPasswordMismatchValidator.directive';
import { RegisterOwnerService } from './../shared/service/register-owner.service';
import { MatDialog } from '@angular/material/dialog';
import {
  DefaultDialogComponent,
  DefaultDialogData,
} from '../../../core/notification/default-dialog/default-dialog.component';
import { DuplicatedPhoneNumberValidator } from '../shared/validator/duplicated-phone-number-validator.directive';
import { DuplicatedEmailValidator } from '../shared/validator/duplicated-email-validator.directive';

@Component({
  selector: 'app-signup-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
  ],
  templateUrl: './signup-form.component.html',
  styleUrl: './signup-form.component.scss',
})
export class SignupFormComponent {
  private registerOwnerService = inject(RegisterOwnerService);
  private duplicatedPhoneNumberValidator = inject(
    DuplicatedPhoneNumberValidator
  );
  private duplicatedEmailValidator = inject(DuplicatedEmailValidator);
  private router = inject(Router);
  private matDialog = inject(MatDialog);

  private destroyRef = inject(DestroyRef);

  fullName = new FormControl('', Validators.required);

  phoneNumber = new FormControl('', {
    validators: [
      Validators.required,
      Validators.pattern('^(?:\\+84|0084|0)[235789][0-9]{8}$'),
    ],
    asyncValidators: [
      this.duplicatedPhoneNumberValidator.validate.bind(
        this.duplicatedPhoneNumberValidator
      ),
    ],
    updateOn: 'blur',
  });

  password = new FormControl('', [
    Validators.required,
    Validators.minLength(8),
    Validators.maxLength(64),
  ]);

  showPassword = signal(false);

  onShowPasswordClick(event: MouseEvent) {
    this.showPassword.set(!this.showPassword());
    event.stopPropagation();
  }

  confirmPassword = new FormControl('', [
    Validators.required,
    confirmPasswordMismatchValidator(this.password),
  ]);

  showConfirmPassword = signal(false);

  onShownConfirmPasswordClick(event: MouseEvent) {
    this.showConfirmPassword.set(!this.showConfirmPassword());
    event.stopPropagation();
  }

  email = new FormControl('', {
    validators: [Validators.email],
    asyncValidators: [
      this.duplicatedEmailValidator.validate.bind(
        this.duplicatedEmailValidator
      ),
    ],
    updateOn: 'blur',
  });

  signupForm = new FormGroup({
    fullName: this.fullName,
    phoneNumber: this.phoneNumber,
    password: this.password,
    confirmPassword: this.confirmPassword,
    email: this.email,
  });

  onSubmit() {
    this.registerOwnerService
      .registerOwner({
        fullName: this.fullName.value ?? '',
        phoneNumber: this.phoneNumber.value ?? '',
        password: this.phoneNumber.value ?? '',
        email: this.email.value ?? '',
      })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        complete: () => this.onRegisterSuccessful(),
        error: () => this.onRegisterFailed(),
      });
  }

  private onRegisterSuccessful(): void {
    const dialogData: DefaultDialogData = {
      type: 'success',
      title: 'Đăng ký thành công',
      content: 'Chuyển sang trang đăng nhập',
    };

    const dialogRef = this.matDialog.open(DefaultDialogComponent, {
      data: dialogData,
    });

    dialogRef
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => {
        this.router.navigate(['/dashboard/login']);
      });
  }

  private onRegisterFailed(): void {
    const dialogData: DefaultDialogData = {
      type: 'error',
      title: 'Đăng ký thất bại',
      content:
        'Vui lòng liên lạc đến bộ phận chăm sóc khách hàng để được hỗ trợ',
    };

    this.matDialog.open(DefaultDialogComponent, {
      data: dialogData,
    });
  }
}
