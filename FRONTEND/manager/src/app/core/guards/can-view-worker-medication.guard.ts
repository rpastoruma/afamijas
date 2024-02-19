import { CanActivateFn } from '@angular/router';

export const canViewWorkerMedicationGuard: CanActivateFn = (route, state) => {
  return true;
};
