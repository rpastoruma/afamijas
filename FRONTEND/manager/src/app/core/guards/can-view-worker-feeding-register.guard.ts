import { CanActivateFn } from '@angular/router';

export const canViewWorkerFeedingRegisterGuard: CanActivateFn = (route, state) => {
  return true;
};
