import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { canViewEnferGuard } from './can-view-enfer.guard';

describe('canViewEnferGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canViewEnferGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
