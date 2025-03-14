import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PaiFisioListComponent } from './pai-fisio-list/pai-fisio-list.component';

const routes: Routes = [
    { path: '', component: PaiFisioListComponent }
  ];


@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
    //providers: [CanUpdateAcademicTitle]
})
export class PaiFisioRoutingModule { }