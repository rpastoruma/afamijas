import { CanActivateFn } from '@angular/router';

export const canViewSocialWorkerGuard: CanActivateFn = (route, state) => {
  return true;
};
