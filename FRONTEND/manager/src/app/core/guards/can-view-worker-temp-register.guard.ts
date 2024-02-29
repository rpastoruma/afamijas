import { CanActivateFn } from '@angular/router';

export const canViewWorkerTempRegisterGuard: CanActivateFn = (route, state) => {
  return true;
};
