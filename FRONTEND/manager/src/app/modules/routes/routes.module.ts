import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RelativeRouteComponent } from './relative-route/relative-route.component';
import { SharedModule } from 'src/app/shared/shared.module';

import { RoutesRoutingModule } from './routes-routing.module'; 
import { FlatpickrModule } from 'angularx-flatpickr';
import { FormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    RelativeRouteComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RoutesRoutingModule,
    FormsModule,FlatpickrModule.forRoot(),
  ]
})
export class RoutesModule { }
