import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { canViewLegionellalogGuard } from './can-view-legionellalog.guard';

describe('canViewLegionellalogGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canViewLegionellalogGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
