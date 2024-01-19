import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AbsencesListComponent } from './absences-list/absences-list.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { AbsencesRoutingModule } from './absences.routing.module';



@NgModule({
  declarations: [
    AbsencesListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    AbsencesRoutingModule
  ]
})
export class AbsencesModule { }
