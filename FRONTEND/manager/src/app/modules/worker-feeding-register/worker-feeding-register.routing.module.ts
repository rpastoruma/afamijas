import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { WorkerFeedingRegisterListComponent } from './worker-feeding-register-list/worker-feeding-register-list.component';


const routes: Routes = [
    { path: '', component: WorkerFeedingRegisterListComponent }
];


@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
    //providers: [CanUpdateAcademicTitle]
})
export class WorkerFeedingRegisterRoutingModule { }

