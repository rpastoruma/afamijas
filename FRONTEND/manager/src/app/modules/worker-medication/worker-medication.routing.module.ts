import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { WorkerMedicationListComponent } from './worker-medication-list/worker-medication-list.component';


const routes: Routes = [
    { path: '', component: WorkerMedicationListComponent }
];


@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
    //providers: [CanUpdateAcademicTitle]
})
export class WorkerMedicationRoutingModule { }

