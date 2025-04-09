import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/shared/shared.module';
import { FormsModule } from '@angular/forms';
import { FlatpickrModule } from 'angularx-flatpickr';

import { PaiEnferListComponent } from './pai-enfer-list/pai-enfer-list.component';
import { PaiEnferRoutingModule } from './pai-enfer.module.routing';


@NgModule({
  declarations: [
    PaiEnferListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    PaiEnferRoutingModule,
    FormsModule,FlatpickrModule.forRoot(),
  ]
})
export class PaiEnferModule { }
 