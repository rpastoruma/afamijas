import { Component, EventEmitter, Input, OnInit, Optional, Output } from '@angular/core';
import { NbDialogRef } from '@nebular/theme';
import { NbDialogService, NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import { SigningPadComponent } from '../signing-pad/signing-pad.component';
@Component({
  selector: 'app-my-pdf-viewer',
  templateUrl: './my-pdf-viewer.component.html',
  styleUrls: ['./my-pdf-viewer.component.scss']
})
export class MyPdfViewerComponent implements OnInit {

  @Input() pdfSrc : string;
  @Input() fullname : string;
  @Input() forSigning : boolean = false;
  @Input() openExternal : boolean = false;

  
  constructor(
    @Optional() private ref: NbDialogRef<MyPdfViewerComponent>,
    private dialogService : NbDialogService

  ) { }

  ngOnInit(): void {
    console.log("pdfSrc=" + this.pdfSrc);
    console.log("fullname=" + this.fullname);
  }

  goBack() {
    this.ref.close('close');
  }


  openSigningPad()
  {
    this.dialogService.open(SigningPadComponent, {
      hasScroll: true,
      closeOnBackdropClick: false,
      closeOnEsc: false,
      context: {
        urlpdf: this.pdfSrc,
        fullname : this.fullname
      }
    }).onClose.subscribe(
      res => {
        if (res !== 'close') {
          this.ref.close(res);
        } 
      }
    );    
      
  }

  openPDFExternal()
  {
    window.open(this.pdfSrc);
  }



}
