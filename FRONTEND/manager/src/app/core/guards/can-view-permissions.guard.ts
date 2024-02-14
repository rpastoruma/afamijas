import { CanActivateFn } from '@angular/router';

export const canViewPermissionsGuard: CanActivateFn = (route, state) => {
  return true;
};
