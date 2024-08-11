import { HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { LoadingSpinnerService } from '../service/loading-spinner.service';
import { finalize } from 'rxjs';

export const loadingSpinnerInterceptor: HttpInterceptorFn = (req, next) => {
  const loadingSpinnerService = inject(LoadingSpinnerService);

  if (shouldShowLoadingSpinner(req)) {
    loadingSpinnerService.show();
    return next(req).pipe(finalize(() => loadingSpinnerService.hide()));
  }

  return next(req);
};

const shouldShowLoadingSpinner = (req: HttpRequest<unknown>): boolean => {
  if (req.url.startsWith('/api')) {
    return true;
  }

  if (req.url.startsWith('/actuator')) {
    return true;
  }

  return false;
};
