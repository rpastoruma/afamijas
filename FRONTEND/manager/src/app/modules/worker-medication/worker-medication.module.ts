import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/shared/shared.module';
import { WorkerMedicationRoutingModule } from './worker-medication.routing.module';
import { FormsModule } from '@angular/forms';
import { WorkerMedicationListComponent } from './worker-medication-list/worker-medication-list.component';

@NgModule({
  declarations: [
    WorkerMedicationListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    WorkerMedicationRoutingModule,
    FormsModule
  ]
})
export class WorkerMedicationModule { }
