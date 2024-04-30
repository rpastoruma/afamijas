
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WorkerDocumentsListComponent } from './worker-documents-list/worker-documents-list.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { WorkerDocumentsRoutingModule } from './worker-documents.routing.module';
import { FormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    WorkerDocumentsListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    WorkerDocumentsRoutingModule,
    FormsModule
  ]
})
export class WorkerDocumentsModule { }
