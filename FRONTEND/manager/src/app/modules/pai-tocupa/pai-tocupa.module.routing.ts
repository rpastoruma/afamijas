import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PaiTocupaListComponent } from './pai-tocupa-list/pai-tocupa-list.component';


const routes: Routes = [
    { path: '', component: PaiTocupaListComponent }
  ];


@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
    //providers: [CanUpdateAcademicTitle]
})
export class PaiTocupaRoutingModule { }