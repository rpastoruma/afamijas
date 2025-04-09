import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/shared/shared.module';
import { FormsModule } from '@angular/forms';
import { FlatpickrModule } from 'angularx-flatpickr';

import { PaiSocialListComponent } from './pai-social-list/pai-social-list.component';
import { PaiSocialRoutingModule } from './pai-social.module.routing';


@NgModule({
  declarations: [
    PaiSocialListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    PaiSocialRoutingModule,
    FormsModule,FlatpickrModule.forRoot(),
  ]
})
export class PaiSocialModule { }
 