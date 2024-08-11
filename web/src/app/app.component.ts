import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LoadingSpinnerComponent } from './core/loading-spinner/loading-spinner.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, LoadingSpinnerComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {}
