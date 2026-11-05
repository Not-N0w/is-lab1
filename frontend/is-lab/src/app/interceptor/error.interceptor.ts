import { HttpContextToken, HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { ErrorService } from '../service/error.service';

export const SKIP_ERROR_DIALOG = new HttpContextToken<boolean>(() => false);

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const errorService = inject(ErrorService);
  return next(req).pipe(
    catchError((e: unknown) => {
      if (e instanceof HttpErrorResponse && !req.context.get(SKIP_ERROR_DIALOG)) {
        errorService.showError(e);
      }
      return throwError(() => e);
    }),
  );
};
