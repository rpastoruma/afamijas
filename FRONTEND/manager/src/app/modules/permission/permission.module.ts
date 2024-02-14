import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/shared/shared.module';
import { PermissionsRoutingModule } from './permissions.routing.module';
import { FlatpickrModule } from 'angularx-flatpickr';
import { FormsModule } from '@angular/forms';
import { PermissionsListComponent } from './permissions-list/permissions-list.component';



@NgModule({
  declarations: [
    PermissionsListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    PermissionsRoutingModule,
    FormsModule,FlatpickrModule.forRoot(),
  ]
})
export class PermissionModule { }
