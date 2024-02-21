import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/shared/shared.module';
import { WorkerFoodRoutingModule } from './worker-food.routing.module';
import { FormsModule } from '@angular/forms';

import { WorkerFoodListComponent } from './worker-food-list/worker-food-list.component';

@NgModule({
  declarations: [
    WorkerFoodListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    WorkerFoodRoutingModule,
    FormsModule
  ]
})
export class WorkerFoodModule { }
