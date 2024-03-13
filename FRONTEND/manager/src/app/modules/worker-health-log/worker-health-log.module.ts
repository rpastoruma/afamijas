
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WorkerHealthLogListComponent } from './worker-health-log-list/worker-health-log-list.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { WorkerHealthLogRoutingModule } from './worker-health-log.routing.module';
import { FormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    WorkerHealthLogListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    WorkerHealthLogRoutingModule,
    FormsModule
  ]
})
export class WorkerHealthLogModule { }
