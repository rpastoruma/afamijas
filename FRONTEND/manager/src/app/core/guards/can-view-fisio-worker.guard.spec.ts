import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { canViewFisioWorkerGuard } from './can-view-fisio-worker.guard';

describe('canViewFisioWorkerGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canViewFisioWorkerGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
