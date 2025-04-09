import { CanActivateFn } from '@angular/router';

export const canViewEnferGuard: CanActivateFn = (route, state) => {
  return true;
};
