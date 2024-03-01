import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WorkerLegionellaListComponent } from './worker-legionella-list/worker-legionella-list.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { WorkerLegionellaRoutingModule } from './worker-legionella.routing.module';
import { FormsModule } from '@angular/forms';




@NgModule({
  declarations: [
    WorkerLegionellaListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    WorkerLegionellaRoutingModule,
    FormsModule
  ]
})
export class WorkerLegionellaModule { }





