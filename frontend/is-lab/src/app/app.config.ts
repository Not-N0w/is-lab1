import { ApplicationConfig, inject, provideAppInitializer, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeuix/themes/aura';
import { routes } from './app.routes';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { FilterService } from 'primeng/api';
import { authInterceptor } from './interceptor/auth-interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    providePrimeNG({ theme: { preset: Aura } }),
    provideHttpClient(),
    provideAppInitializer(() => {
      inject(FilterService).register(
        'exact',
        (value: unknown, filter: unknown) =>
          filter === null ||
          filter === undefined ||
          filter === '' ||
          String(value) === String(filter),
      );
    }),
    provideRouter(routes, withComponentInputBinding()),
    provideHttpClient(withInterceptors([authInterceptor])),
  ],
};
