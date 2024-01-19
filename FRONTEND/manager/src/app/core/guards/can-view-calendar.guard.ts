import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service'; 
//import {  hasRole, RoleCode } from '../../shared/models/models';

export const canViewCalendarGuard: CanActivateFn = (route, state) => {

  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated()) return true;

  router.navigate(['/login']); // , { queryParams: { returnUrl: state.url } }
  return false;

};
