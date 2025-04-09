import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { canViewPaiGuard } from './can-view-pai.guard';

describe('canViewPaiGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canViewPaiGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
