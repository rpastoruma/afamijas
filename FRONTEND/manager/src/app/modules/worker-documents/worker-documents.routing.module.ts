import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
//import { CanUpdateAcademicTitle } from 'src/app/core/guards/academic-title/canUpdateAcademicTitle';
import { WorkerDocumentsListComponent } from './worker-documents-list/worker-documents-list.component';

const routes: Routes = [
    { path: '', component: WorkerDocumentsListComponent }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
    //providers: [CanUpdateAcademicTitle]
})
export class WorkerDocumentsRoutingModule { }