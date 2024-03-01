import { CanActivateFn } from '@angular/router';

export const canViewLegionellalogGuard: CanActivateFn = (route, state) => {
  return true;
};
