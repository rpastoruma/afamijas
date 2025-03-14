import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/shared/shared.module';
import { MenusRoutingModule } from './menus.routing.module';
import { FlatpickrModule } from 'angularx-flatpickr';
import { FormsModule } from '@angular/forms';
import { MenusListComponent } from './menus-list/menus-list.component';



@NgModule({
  declarations: [
    MenusListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    MenusRoutingModule,
    FormsModule,FlatpickrModule.forRoot(),
  ]
})
export class MenusModule { }


