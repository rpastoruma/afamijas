import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PaiSocialListComponent } from './pai-social-list/pai-social-list.component';


const routes: Routes = [
    { path: '', component: PaiSocialListComponent }
  ];


@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
    //providers: [CanUpdateAcademicTitle]
})
export class PaiSocialRoutingModule { }