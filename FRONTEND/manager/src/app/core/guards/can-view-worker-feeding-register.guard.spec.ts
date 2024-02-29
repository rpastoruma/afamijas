import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { canViewWorkerFeedingRegisterGuard } from './can-view-worker-feeding-register.guard';

describe('canViewWorkerFeedingRegisterGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canViewWorkerFeedingRegisterGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
