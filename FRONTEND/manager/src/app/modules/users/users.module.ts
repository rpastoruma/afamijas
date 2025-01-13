import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsersListComponent } from './users-list/users-list.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { FormsModule } from '@angular/forms';
import { FlatpickrModule } from 'angularx-flatpickr';
import { UsersRoutingModule } from './users.routing.module';


@NgModule({
  declarations: [
    UsersListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    UsersRoutingModule,
    FormsModule,FlatpickrModule.forRoot(),
  ]
})
export class UsersModule { }
 