import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { NbAuthComponent } from '@nebular/auth';  // <---
import { LoginComponent } from './login/login.component';
import { RequestPasswordComponent } from './request-password/request-password.component';
import { ChangePasswordComponent } from './change-password/change-password.component';

export const routes: Routes = [
    {
      path: '',
      component: NbAuthComponent,
      children: [
        {
          path: 'login',
          component: LoginComponent, // <---
        },
        {
          path: 'request-password',
          component: RequestPasswordComponent, // <---
        },
        {
          path: 'change-pass',
          component: ChangePasswordComponent, // <---
        },
        {
            path: '**',
            redirectTo: 'login', // <---
        },
      ],
    },
  ];
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class NgxAuthRoutingModule {
}