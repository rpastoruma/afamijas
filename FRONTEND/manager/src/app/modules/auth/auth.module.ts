import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LoginComponent } from './login/login.component'; 
import { SharedModule } from 'src/app/shared/shared.module';
import { NgxAuthRoutingModule } from './auth-routing.module';
import { NbAuthModule } from '@nebular/auth';


@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    NgxAuthRoutingModule,
    NbAuthModule,
    NgxAuthRoutingModule,
    NbAuthModule,
  ],
  declarations: [
    // ... here goes our new components
  
    LoginComponent
  ],
})
export class AuthModule {
}