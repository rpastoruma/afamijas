import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
//import { CanUpdateAcademicTitle } from 'src/app/core/guards/academic-title/canUpdateAcademicTitle';
import { AbsencesWorkerListComponent } from './absences-worker-list/absences-worker-list.component';

const routes: Routes = [
    { path: '', component: AbsencesWorkerListComponent }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
    //providers: [CanUpdateAcademicTitle]
})
export class AbsencesWorkerRoutingModule { }