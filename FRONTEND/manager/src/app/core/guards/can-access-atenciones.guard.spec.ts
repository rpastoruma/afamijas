import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { canAccessAtencionesGuard } from './can-access-atenciones.guard';

describe('canAccessAtencionesGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canAccessAtencionesGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
