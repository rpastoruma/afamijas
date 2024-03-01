import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { WorkerLegionellaListComponent } from './worker-legionella-list/worker-legionella-list.component';

const routes: Routes = [
    { path: '', component: WorkerLegionellaListComponent }
];


@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
    //providers: [CanUpdateAcademicTitle]
})
export class WorkerLegionellaRoutingModule { }

