import { CanActivateFn } from '@angular/router';

export const canViewAbsencesGuard: CanActivateFn = (route, state) => {
  return true;
};
