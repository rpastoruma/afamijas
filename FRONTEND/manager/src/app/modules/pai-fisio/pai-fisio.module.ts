
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/shared/shared.module';
import { FormsModule } from '@angular/forms';
import { FlatpickrModule } from 'angularx-flatpickr';

import { PaiFisioListComponent } from './pai-fisio-list/pai-fisio-list.component';
import { PaiFisioRoutingModule } from './pai-fisio.module.routing';


@NgModule({
  declarations: [
    PaiFisioListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    PaiFisioRoutingModule,
    FormsModule,FlatpickrModule.forRoot(),
  ]
})
export class PaiFisioModule { }
 