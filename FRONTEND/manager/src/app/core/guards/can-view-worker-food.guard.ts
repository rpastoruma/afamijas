import { CanActivateFn } from '@angular/router';

export const canViewWorkerFoodGuard: CanActivateFn = (route, state) => {
  return true;
};
