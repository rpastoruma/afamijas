
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/shared/shared.module';
import { RelativesRoutingModule } from './relatives.routing.module';
import { FormsModule } from '@angular/forms';
import { FlatpickrModule } from 'angularx-flatpickr';
import { RelativesListComponent } from './relatives-list/relatives-list.component';


@NgModule({
  declarations: [
    RelativesListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RelativesRoutingModule,
    FormsModule,FlatpickrModule.forRoot(),
  ]
})
export class RelativesModule { }
