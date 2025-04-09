import { CanActivateFn } from '@angular/router';

export const canViewTocupaGuard: CanActivateFn = (route, state) => {
  return true;
};
