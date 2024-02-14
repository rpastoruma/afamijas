import { CanActivateFn } from '@angular/router';

export const canViewMenuGuard: CanActivateFn = (route, state) => {
  return true;
};
