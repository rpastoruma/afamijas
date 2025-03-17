import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PaiPsicoListComponent } from './pai-psico-list/pai-psico-list.component';

const routes: Routes = [
    { path: '', component: PaiPsicoListComponent }
  ];


@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
    //providers: [CanUpdateAcademicTitle]
})
export class PaiPsicoRoutingModule { }