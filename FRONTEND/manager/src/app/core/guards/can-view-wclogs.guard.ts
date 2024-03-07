import { CanActivateFn } from '@angular/router';

export const canViewWclogsGuard: CanActivateFn = (route, state) => {
  return true;
};
