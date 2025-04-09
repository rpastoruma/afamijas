import { CanActivateFn } from '@angular/router';

export const canViewTsocialGuard: CanActivateFn = (route, state) => {
  return true;
};
