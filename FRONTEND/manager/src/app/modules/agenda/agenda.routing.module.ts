import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AgendaListComponent } from './agenda-list/agenda-list.component';

const routes: Routes = [
    { path: '', component: AgendaListComponent }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
    //providers: [CanUpdateAcademicTitle]
})
export class AgendaRoutingModule { }