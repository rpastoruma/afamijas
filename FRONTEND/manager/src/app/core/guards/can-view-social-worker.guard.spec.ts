import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { canViewSocialWorkerGuard } from './can-view-social-worker.guard';

describe('canViewSocialWorkerGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canViewSocialWorkerGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
