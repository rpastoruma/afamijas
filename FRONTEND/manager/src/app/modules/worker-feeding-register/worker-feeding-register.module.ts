import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WorkerFeedingRegisterListComponent } from './worker-feeding-register-list/worker-feeding-register-list.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { WorkerFeedingRegisterRoutingModule } from './worker-feeding-register.routing.module';
import { FormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    WorkerFeedingRegisterListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    WorkerFeedingRegisterRoutingModule,
    FormsModule
  ]
})
export class WorkerFeedingRegisterModule { }
