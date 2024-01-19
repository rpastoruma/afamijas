import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CalendarViewComponent } from './calendar-view/calendar-view.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { CalendarRoutingModule } from './calendar.routing.module';



@NgModule({
  declarations: [
    CalendarViewComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    CalendarRoutingModule
  ]
})
export class CalendarModule { }
