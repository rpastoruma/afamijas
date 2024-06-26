import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MembersListComponent } from './members-list/members-list.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { MembersRoutingModule } from './members.routing.module';
import { FormsModule } from '@angular/forms';
import { FlatpickrModule } from 'angularx-flatpickr';
import { MembersReceiptsListComponent } from './members-receipts-list/members-receipts-list.component';



@NgModule({
  declarations: [
    MembersListComponent,
    MembersReceiptsListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    MembersRoutingModule,
    FormsModule,FlatpickrModule.forRoot(),
  ]
})
export class MembersModule { }
