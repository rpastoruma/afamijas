import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { canViewTocupaGuard } from './can-view-tocupa.guard';

describe('canViewTocupaGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canViewTocupaGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
