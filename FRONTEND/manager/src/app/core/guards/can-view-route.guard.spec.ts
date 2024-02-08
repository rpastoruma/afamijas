import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { canViewRouteGuard } from './can-view-route.guard';

describe('canViewRouteGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canViewRouteGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
