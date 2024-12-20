import { CanActivateFn } from '@angular/router';

export const canViewDocsPsicoGuard: CanActivateFn = (route, state) => {
  return true;
};
