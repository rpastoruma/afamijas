import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WorkerTempRegisterListComponent } from './worker-temp-register-list/worker-temp-register-list.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { WorkerTempRegisterRoutingModule } from './worker-temp-register.routing.module';
import { FormsModule } from '@angular/forms';
import { NbTabsetModule } from '@nebular/theme';


@NgModule({
  declarations: [
    WorkerTempRegisterListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    WorkerTempRegisterRoutingModule,
    FormsModule,
    NbTabsetModule 
  ]
})
export class WorkerTempRegisterModule { }
