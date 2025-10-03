
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/shared/shared.module';
import { VolunteersRoutingModule } from './volunteers.routing.module';
import { FormsModule } from '@angular/forms';
import { FlatpickrModule } from 'angularx-flatpickr';
import { VolunteersListComponent } from './volunteers-list/volunteers-list.component';



@NgModule({
  declarations: [
    VolunteersListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    VolunteersRoutingModule,
    FormsModule,FlatpickrModule.forRoot(),
  ]
})
export class VolunteersModule { }
