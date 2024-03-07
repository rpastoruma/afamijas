import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { canViewWclogsGuard } from './can-view-wclogs.guard';

describe('canViewWclogsGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canViewWclogsGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
