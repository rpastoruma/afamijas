import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/shared/shared.module';
import { AtencionesRoutingModule } from './atenciones.routing.module';
import { FormsModule } from '@angular/forms';
import { FlatpickrModule } from 'angularx-flatpickr';
import { AtencionesListComponent } from './atenciones-list/atenciones-list.component';



@NgModule({
  declarations: [
    AtencionesListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    AtencionesRoutingModule,
    FormsModule,FlatpickrModule.forRoot(),
  ]
})
export class AtencionesModule { }

