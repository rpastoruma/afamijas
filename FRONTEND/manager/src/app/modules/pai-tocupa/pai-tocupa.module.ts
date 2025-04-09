import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/shared/shared.module';
import { FormsModule } from '@angular/forms';
import { FlatpickrModule } from 'angularx-flatpickr';

import { PaiTocupaListComponent } from './pai-tocupa-list/pai-tocupa-list.component';
import { PaiTocupaRoutingModule } from './pai-tocupa.module.routing';


@NgModule({
  declarations: [
    PaiTocupaListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    PaiTocupaRoutingModule,
    FormsModule,FlatpickrModule.forRoot(),
  ]
})
export class PaiTocupaModule { }
 