import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/shared/shared.module';
import { FormsModule } from '@angular/forms';
import { FlatpickrModule } from 'angularx-flatpickr';

import { PaiPsicoListComponent } from './pai-psico-list/pai-psico-list.component';
import { PaiPsicoRoutingModule } from './pai-psico.module.routing';


@NgModule({
  declarations: [
    PaiPsicoListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    PaiPsicoRoutingModule,
    FormsModule,FlatpickrModule.forRoot(),
  ]
})
export class PaiPsicoModule { }
 