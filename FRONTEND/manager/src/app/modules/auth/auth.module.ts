import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LoginComponent } from './login/login.component'; 
import { SharedModule } from 'src/app/shared/shared.module';
import { NgxAuthRoutingModule } from './auth-routing.module';
import { NbAuthModule } from '@nebular/auth';
import { RequestPasswordComponent } from './request-password/request-password.component';
import { RecaptchaModule, RecaptchaFormsModule, RECAPTCHA_LANGUAGE } from 'ng-recaptcha';

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    NgxAuthRoutingModule,
    NbAuthModule,
    NbAuthModule,
    RecaptchaModule,
    RecaptchaFormsModule
  ],
  declarations: [
    // ... here goes our new components
  
    LoginComponent,
    RequestPasswordComponent
  ],
  providers: [
    {
      provide: RECAPTCHA_LANGUAGE,
      useValue: 'es',
    },
  ]
})
export class AuthModule {
}