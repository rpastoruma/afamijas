import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { canViewTsocialGuard } from './can-view-tsocial.guard';

describe('canViewTsocialGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canViewTsocialGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
