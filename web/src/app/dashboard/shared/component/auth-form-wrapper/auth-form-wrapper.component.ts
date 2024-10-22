import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-auth-form-wrapper',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './auth-form-wrapper.component.html',
  styleUrl: './auth-form-wrapper.component.scss',
})
export class AuthFormWrapperComponent {
  @Input() title = '';
}
