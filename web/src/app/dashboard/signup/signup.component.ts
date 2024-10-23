import { Component } from '@angular/core';
import { SignupFormComponent } from './signup-form/signup-form.component';
import { AuthFormWrapperComponent } from '../shared/component/auth-form-wrapper/auth-form-wrapper.component';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [SignupFormComponent, AuthFormWrapperComponent],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss',
})
export class SignupComponent {}
