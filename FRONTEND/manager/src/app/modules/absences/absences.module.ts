import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AbsencesListComponent } from './absences-list/absences-list.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { AbsencesRoutingModule } from './absences.routing.module';
import { FlatpickrModule } from 'angularx-flatpickr';
import { FormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    AbsencesListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    AbsencesRoutingModule,
    FormsModule,FlatpickrModule.forRoot(),
  ]
})
export class AbsencesModule { }
