import { CanActivateFn } from '@angular/router';

export const canViewPaiGuard: CanActivateFn = (route, state) => {
  return true;
};
