import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
//import { CanUpdateAcademicTitle } from 'src/app/core/guards/academic-title/canUpdateAcademicTitle';
import { StaffListComponent } from './staff-list/staff-list.component';
import { StaffNominasListComponent } from './staff-nominas-list/staff-nominas-list.component';

const routes: Routes = [
    { path: '', component: StaffListComponent },
        { path: 'nominas', component: StaffNominasListComponent }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
    //providers: [CanUpdateAcademicTitle]
})
export class StaffRoutingModule { }