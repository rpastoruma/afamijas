import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { canViewMenuGuard } from './can-view-menu.guard';

describe('canViewMenuGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canViewMenuGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
