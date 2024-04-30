import { CanActivateFn } from '@angular/router';

export const canAccessMembersGuard: CanActivateFn = (route, state) => {
  return true;
};
