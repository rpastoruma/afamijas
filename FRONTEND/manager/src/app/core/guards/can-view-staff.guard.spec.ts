import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { canViewStaffGuard } from './can-view-staff.guard';

describe('canViewStaffGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canViewStaffGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
