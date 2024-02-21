import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { WorkerFoodListComponent } from './worker-food-list/worker-food-list.component';


const routes: Routes = [
    { path: '', component: WorkerFoodListComponent }
];


@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
    //providers: [CanUpdateAcademicTitle]
})
export class WorkerFoodRoutingModule { }

