import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PaiEnferListComponent } from './pai-enfer-list/pai-enfer-list.component';


const routes: Routes = [
    { path: '', component: PaiEnferListComponent }
  ];


@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
    //providers: [CanUpdateAcademicTitle]
})
export class PaiEnferRoutingModule { }