import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { WorkerHealthLogListComponent } from './worker-health-log-list/worker-health-log-list.component';


const routes: Routes = [
    { path: '', component: WorkerHealthLogListComponent }
];


@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
    //providers: [CanUpdateAcademicTitle]
})
export class WorkerHealthLogRoutingModule { }

