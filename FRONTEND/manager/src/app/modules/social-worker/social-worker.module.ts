import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/shared/shared.module';
import { FormsModule } from '@angular/forms';
import { FlatpickrModule } from 'angularx-flatpickr';

import { SocialWorkerListComponent } from './social-worker-list/social-worker-list.component';
import { SocialWorkerRoutingModule } from './social-worker.module.routing';



@NgModule({
  declarations: [
    SocialWorkerListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    SocialWorkerRoutingModule,
    FormsModule,FlatpickrModule.forRoot(),
  ]
})
export class SocialWorkerModule { }
 