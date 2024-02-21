import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { canViewWorkerFoodGuard } from './can-view-worker-food.guard';

describe('canViewWorkerFoodGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canViewWorkerFoodGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
