import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AgendaListComponent } from './agenda-list/agenda-list.component';
import { AgendaRoutingModule } from './agenda.routing.module';
import { SharedModule } from 'src/app/shared/shared.module';

@NgModule({
  declarations: [
    AgendaListComponent
  ],
  imports: [
    CommonModule,
    AgendaRoutingModule,
    SharedModule
  ]
})
export class AgendaModule { }
