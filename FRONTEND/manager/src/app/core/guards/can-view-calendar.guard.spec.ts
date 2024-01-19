import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { canViewCalendarGuard } from './can-view-calendar.guard';

describe('canViewCalendarGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canViewCalendarGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
