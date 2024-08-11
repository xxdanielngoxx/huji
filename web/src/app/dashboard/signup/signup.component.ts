import { Component } from '@angular/core';
import { SignupFormComponent } from './signup-form/signup-form.component';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [SignupFormComponent, RouterLink],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss',
})
export class SignupComponent {}
