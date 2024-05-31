import { CanActivateFn } from '@angular/router';

export const canAccessInvoicesGuard: CanActivateFn = (route, state) => {
  return true;
};
