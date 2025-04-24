import { CanActivateFn } from '@angular/router';

export const canViewStaffGuard: CanActivateFn = (route, state) => {
  return true;
};
