import { CanActivateFn } from '@angular/router';

export const canViewHealthLogsGuard: CanActivateFn = (route, state) => {
  return true;
};
