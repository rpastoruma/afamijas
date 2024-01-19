import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { NbAuthComponent } from '@nebular/auth';  // <---
import { LoginComponent } from './login/login.component';
import { RequestPasswordComponent } from './request-password/request-password.component';

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