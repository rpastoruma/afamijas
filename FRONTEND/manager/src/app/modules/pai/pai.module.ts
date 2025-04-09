import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/shared/shared.module';
import { FormsModule } from '@angular/forms';
import { FlatpickrModule } from 'angularx-flatpickr';

import { PaiListComponent } from './pai-list/pai-list.component';
import { PaiRoutingModule } from './pai.module.routing';



@NgModule({
  declarations: [
    PaiListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    PaiRoutingModule,
    FormsModule,FlatpickrModule.forRoot(),
  ]
})
export class PaiModule { }
 