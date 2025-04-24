
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StaffListComponent } from './staff-list/staff-list.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { StaffRoutingModule } from './staff.routing.module';
import { FormsModule } from '@angular/forms';
import { FlatpickrModule } from 'angularx-flatpickr';
import { StaffNominasListComponent } from './staff-nominas-list/staff-nominas-list.component';




@NgModule({
  declarations: [
    StaffListComponent,
    StaffNominasListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    StaffRoutingModule,
    FormsModule,FlatpickrModule.forRoot(),
  ]
})
export class StaffModule { }
