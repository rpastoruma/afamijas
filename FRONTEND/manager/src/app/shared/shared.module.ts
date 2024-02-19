import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedRoutingModule } from './shared-routing.module';
import { NbActionsModule, NbAlertModule, NbButtonModule, NbCardModule, NbCheckboxModule, NbDialogModule, NbIconModule,
  NbInputModule, NbLayoutModule, NbPopoverModule, NbSelectModule, NbSpinnerModule, NbTooltipModule } from '@nebular/theme';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

//import { PaginationComponent } from './components/pagination/pagination.component';
//import { FillPipe } from './pipes/fill.pipe';
import {MatDatepickerModule} from '@angular/material/datepicker';
import { MatSelectModule } from '@angular/material/select';
import { NgSelectModule } from '@ng-select/ng-select';
//import { SelectorCountriesComponent } from './components/selector-countries/selector-countries.component';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { SigningPadComponent } from './components/signing-pad/signing-pad.component';
import { DeleteConfirmComponent } from './components/delete-confirm/delete-confirm.component';
//import { DeleteConfirmComponent } from './components/delete-confirm/delete-confirm.component';
//import { TableFieldListModalComponent } from './components/table-field-list-modal/table-field-list-modal.component';
import { SafeUrlPipe } from './pipes/safeUrl.pipe';


import { FlatpickrModule } from 'angularx-flatpickr';
import flatpickr from 'flatpickr';
import { Spanish } from 'flatpickr/dist/l10n/es';
import { CustomTableComponent } from './components/custom-table/custom-table.component';
import { PaginationComponent } from './components/pagination/pagination.component';
import { PdfViewerModule } from 'ng2-pdf-viewer';
import { MyPdfViewerComponent } from './components/my-pdf-viewer/my-pdf-viewer.component';

import { NgxExtendedPdfViewerModule } from 'ngx-extended-pdf-viewer';


@NgModule({
    declarations: [
      //PaginationComponent,
      //FillPipe,
      //ResearchersSelectorComponent,
      //DeleteConfirmComponent,
      SafeUrlPipe,
    
  
    SigningPadComponent,
      DeleteConfirmComponent,
      CustomTableComponent,
      PaginationComponent,
      MyPdfViewerComponent,
  ],
    imports: [
      CommonModule,
      SharedRoutingModule,
      NbDialogModule.forChild(),
      NbLayoutModule,
      NbCardModule,
      RouterModule,
      NbAlertModule,
      FormsModule,FlatpickrModule.forRoot(),
      ReactiveFormsModule,
      NbInputModule,
      NbButtonModule,
      NbSelectModule,
      NbCheckboxModule,
      NbActionsModule,
      NbIconModule,
      NbTooltipModule,
      MatDatepickerModule,
      MatSelectModule,
      NgSelectModule,
      NbPopoverModule,
      DragDropModule,
      NbSpinnerModule,
      PdfViewerModule,
      NgxExtendedPdfViewerModule
    ],
    exports: [
      CommonModule,
      NbLayoutModule,
      NbCardModule,
      RouterModule,
      NbAlertModule,
      FormsModule, FlatpickrModule,
      ReactiveFormsModule,
      NbInputModule,
      CustomTableComponent,
      NbButtonModule,
      MatSelectModule,
      NbSelectModule,
      NbCheckboxModule,
      NbActionsModule, 
      NbIconModule, 
      NgSelectModule,
      MatDatepickerModule,
      //SelectorCountriesComponent,
      NbPopoverModule,
      DragDropModule,
      
      //PaginationComponent,
    ],
    //entryComponents: [
      //DeleteConfirmComponent,
      //TableFieldListModalComponent,
    //] 
})
export class SharedModule { }

