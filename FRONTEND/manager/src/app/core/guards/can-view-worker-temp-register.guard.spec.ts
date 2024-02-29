import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { canViewWorkerTempRegisterGuard } from './can-view-worker-temp-register.guard';

describe('canViewWorkerTempRegisterGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canViewWorkerTempRegisterGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
