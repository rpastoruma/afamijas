import { CanActivateFn } from '@angular/router';

export const canAccessAtencionesGuard: CanActivateFn = (route, state) => {
  return true;
};
