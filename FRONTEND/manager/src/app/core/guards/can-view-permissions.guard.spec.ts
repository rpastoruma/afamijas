import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { canViewPermissionsGuard } from './can-view-permissions.guard';

describe('canViewPermissionsGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canViewPermissionsGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
