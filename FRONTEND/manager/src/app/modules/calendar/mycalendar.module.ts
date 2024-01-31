import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CalendarViewComponent } from './calendar-view/calendar-view.component';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';

import { FormsModule } from '@angular/forms';

import { FlatpickrModule } from 'angularx-flatpickr';

import { SharedModule } from 'src/app/shared/shared.module';
import { MyCalendarRoutingModule  } from './mycalendar.routing.module';
import flatpickr from 'flatpickr';
import { Spanish } from 'flatpickr/dist/l10n/es';
import { NbContextMenuModule } from '@nebular/theme';

@NgModule({
  declarations: [
    CalendarViewComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    MyCalendarRoutingModule,
    CalendarModule.forRoot({
      provide: DateAdapter,
      useFactory: adapterFactory,
          }),
          FormsModule,FlatpickrModule.forRoot(),
          NbContextMenuModule

  ]
})
export class MyCalendarModule { }

export function flatpickrFactory() { 
  flatpickr.localize(Spanish);
  return flatpickr;
}