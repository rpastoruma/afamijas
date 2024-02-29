import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { WorkerTempRegisterListComponent } from './worker-temp-register-list/worker-temp-register-list.component';


const routes: Routes = [
    { path: '', component: WorkerTempRegisterListComponent }
];


@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
    //providers: [CanUpdateAcademicTitle]
})
export class WorkerTempRegisterRoutingModule { }

