import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WorkerWclogListComponent } from './worker-wclog-list/worker-wclog-list.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { WorkerWclogaRoutingModule } from './worker-wclog.routing.module';
import { FormsModule } from '@angular/forms';




@NgModule({
  declarations: [
    WorkerWclogListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    WorkerWclogaRoutingModule,
    FormsModule
  ]
})
export class WorkerWclogModule { }





