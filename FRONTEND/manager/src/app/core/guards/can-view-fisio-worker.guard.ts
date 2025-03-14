import { CanActivateFn } from '@angular/router';

export const canViewFisioWorkerGuard: CanActivateFn = (route, state) => {
  return true;
};
