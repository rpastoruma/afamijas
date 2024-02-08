import { CanActivateFn } from '@angular/router';

export const canViewRouteGuard: CanActivateFn = (route, state) => {
  return true;
};
