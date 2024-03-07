import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { WorkerWclogListComponent } from './worker-wclog-list/worker-wclog-list.component';

const routes: Routes = [
    { path: '', component: WorkerWclogListComponent }
];


@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
    //providers: [CanUpdateAcademicTitle]
})
export class WorkerWclogaRoutingModule { }

