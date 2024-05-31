import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/shared/shared.module';
import { InvoicesRoutingModule } from './invoices.routing.module';
import { FormsModule } from '@angular/forms';
import { FlatpickrModule } from 'angularx-flatpickr';
import { InvoicesListComponent } from './invoices-list/invoices-list.component';
 

@NgModule({
  declarations: [
    InvoicesListComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    InvoicesRoutingModule,
    FormsModule,FlatpickrModule.forRoot(),
  ]
})
export class InvoicesModule { }
