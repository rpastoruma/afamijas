import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { canViewWorkerMedicationGuard } from './can-view-worker-medication.guard';

describe('canViewWorkerMedicationGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canViewWorkerMedicationGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
