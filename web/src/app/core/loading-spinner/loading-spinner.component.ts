import { Component, computed, inject } from '@angular/core';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { LoadingSpinnerService } from './service/loading-spinner.service';

@Component({
  selector: 'app-loading-spinner',
  standalone: true,
  imports: [MatProgressSpinnerModule],
  templateUrl: './loading-spinner.component.html',
  styleUrl: './loading-spinner.component.scss',
})
export class LoadingSpinnerComponent {
  loadingSpinnerService = inject(LoadingSpinnerService);
  isShow = computed(this.loadingSpinnerService.isShow);
}
