import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';
import { map } from 'rxjs/operators';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.isLoggedIn().pipe(
    map(isAuthenticated => {
      if (!isAuthenticated) {
        // Guardar la URL a la que intentaba acceder
        authService.redirectUrl = state.url;
        // Redirigir al login
        return router.createUrlTree(['/login']);
      }
      return true;
    })
  );
};
