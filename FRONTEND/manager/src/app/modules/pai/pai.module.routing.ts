import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PaiListComponent } from './pai-list/pai-list.component';


const routes: Routes = [
    { path: '', component: PaiListComponent }
  ];


@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
    //providers: [CanUpdateAcademicTitle]
})
export class PaiRoutingModule { }