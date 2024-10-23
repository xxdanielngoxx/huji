import { Component } from '@angular/core';
import { AuthFormWrapperComponent } from '../shared/component/auth-form-wrapper/auth-form-wrapper.component';
import { LoginFormComponent } from './login-form/login-form.component';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [AuthFormWrapperComponent, LoginFormComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {}
