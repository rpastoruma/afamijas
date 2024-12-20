import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SocialWorkerListComponent } from './social-worker-list/social-worker-list.component';

const routes: Routes = [
    { path: '', component: SocialWorkerListComponent }
  ];


@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
    //providers: [CanUpdateAcademicTitle]
})
export class  SocialWorkerRoutingModule { }