import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
//import { CanUpdateAcademicTitle } from 'src/app/core/guards/academic-title/canUpdateAcademicTitle';
import { VolunteersListComponent } from './volunteers-list/volunteers-list.component';

const routes: Routes = [
    { path: '', component: VolunteersListComponent },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
    //providers: [CanUpdateAcademicTitle]
})
export class VolunteersRoutingModule { }