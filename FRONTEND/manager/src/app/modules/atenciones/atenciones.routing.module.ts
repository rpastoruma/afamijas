import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
//import { CanUpdateAcademicTitle } from 'src/app/core/guards/academic-title/canUpdateAcademicTitle';
import { AtencionesListComponent } from './atenciones-list/atenciones-list.component';

const routes: Routes = [
    { path: '', component: AtencionesListComponent }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
    //providers: [CanUpdateAcademicTitle]
})
export class AtencionesRoutingModule { }