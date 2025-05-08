import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AbsencesWorkerListComponent } from './absences-worker-list/absences-worker-list.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { AbsencesWorkerRoutingModule } from './absences-worker.routing.module';
import { FlatpickrModule } from 'angularx-flatpickr';
import { FormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    AbsencesWorkerListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    AbsencesWorkerRoutingModule,
    FormsModule,FlatpickrModule.forRoot(),
  ]
})
export class AbsencesWorkerModule { }
