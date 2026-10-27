import { HttpErrorResponse, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, switchMap, throwError } from 'rxjs';
import { AuthService } from '../service/AuthService';

function withToken(req: HttpRequest<unknown>, token: string | null) {
  if (!token) return req;
  return req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
}

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (req.url.includes('/auth/public')) {
    return next(req);
  }

  return next(withToken(req, authService.getAccessToken())).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status !== 401 || !authService.getRefreshToken()) {
        return throwError(() => error);
      }

      return authService.refresh().pipe(
        switchMap((res) => next(withToken(req, res.accessToken))),
        catchError((refreshError) => {
          authService.logout();
          router.navigate(['/auth']);
          return throwError(() => refreshError);
        }),
      );
    }),
  );
};
