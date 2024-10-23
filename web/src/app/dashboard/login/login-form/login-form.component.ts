import { Component, DestroyRef, inject, signal } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { AuthenticationService } from '../shared/service/authentication.service';
import { MatDialog } from '@angular/material/dialog';
import {
  DefaultDialogComponent,
  DefaultDialogData,
} from '../../../core/notification/default-dialog/default-dialog.component';
import { Router } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-login-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
  ],
  templateUrl: './login-form.component.html',
  styleUrl: './login-form.component.scss',
})
export class LoginFormComponent {
  private readonly authenticationService = inject(AuthenticationService);

  private readonly matDialog = inject(MatDialog);

  private readonly router = inject(Router);

  private readonly destroyRef = inject(DestroyRef);

  username = new FormControl('', [Validators.required]);

  password = new FormControl('', [Validators.required]);

  showPassword = signal(false);

  onShowPasswordClick(event: MouseEvent) {
    this.showPassword.set(!this.showPassword());
    event.stopPropagation();
  }

  loginForm = new FormGroup({
    username: this.username,
    password: this.password,
  });

  onSubmit() {
    this.authenticationService
      .loginWithUsernamePassword({
        username: this.username.value ?? '',
        password: this.password.value ?? '',
      })
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        complete: () => this.onLoginSuccess(),
        error: () => this.onLoginFail(),
      });
  }

  private onLoginSuccess(): void {
    this.router.navigate(['/dashboard']);
  }

  private onLoginFail(): void {
    const dialogData: DefaultDialogData = {
      type: 'error',
      title: 'Đăng nhập thất bại',
      content: 'Tên đăng nhập hoặc mật khẩu không đúng',
      isDisplayCloseButton: true,
    };

    this.matDialog.open(DefaultDialogComponent, {
      data: dialogData,
    });
  }
}
