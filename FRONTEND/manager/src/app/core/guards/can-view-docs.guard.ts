import { CanActivateFn } from '@angular/router';

export const canViewDocsGuard: CanActivateFn = (route, state) => {
  return true;
};
