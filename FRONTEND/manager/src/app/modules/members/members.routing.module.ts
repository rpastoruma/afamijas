import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
//import { CanUpdateAcademicTitle } from 'src/app/core/guards/academic-title/canUpdateAcademicTitle';
import { MembersListComponent } from './members-list/members-list.component';
import { MembersReceiptsListComponent } from './members-receipts-list/members-receipts-list.component';

const routes: Routes = [
    { path: '', component: MembersListComponent },
    { path: 'receipts', component: MembersReceiptsListComponent }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
    //providers: [CanUpdateAcademicTitle]
})
export class MembersRoutingModule { }