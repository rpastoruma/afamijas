import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { canAccessInvoicesGuard } from './can-access-invoices.guard';

describe('canAccessInvoicesGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canAccessInvoicesGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
