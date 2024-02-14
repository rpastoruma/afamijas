import {Component, ElementRef, EventEmitter, HostListener, Input, OnInit, Optional, Output, ViewChild} from '@angular/core';
import { NbDialogRef, NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import { PDFDocument, StandardFonts, degrees, rgb } from 'pdf-lib'
import { saveAs } from 'file-saver';
import { LocalStorageService } from 'src/app/core/services/local-storage.service';
import { LoginResponse } from '../../models/models';
import { MediaService } from 'src/app/core/services/media.service';

@Component({
  selector: 'app-signing-pad',
  templateUrl: './signing-pad.component.html',
  styleUrls: ['./signing-pad.component.scss']
})
export class SigningPadComponent implements OnInit{

  @Input() urlpdf : string;
  @ViewChild('signPad', {static: false}) signPad!: ElementRef<HTMLCanvasElement>;
 
  private signatureImg?: string;
  private sigPadElement: any;
  private context: any;
  private isDrawing!: boolean;
  isProcessing : boolean = false;
  private drawn : boolean = false;

  constructor(
    @Optional() private ref: NbDialogRef<SigningPadComponent>,
    private localStorageService: LocalStorageService,
    private mediaService : MediaService,
    private toastService : NbToastrService
  ) { }


  ngOnInit(): void {
    this.drawn = false;
  }


  public ngAfterViewInit(): void {
    this.sigPadElement = this.signPad.nativeElement;
    this.context = this.sigPadElement.getContext('2d');
    this.context.strokeStyle = '#000';
  }

  onMouseDown(e: any): void {
    // The mouse button is clicked, which means the start of drawing the signature
    this.isDrawing = true;
    const coords = this.relativeCoords(e);
    this.context.moveTo(coords.x, coords.y);
  }

  @HostListener('document:mouseup', ['$event'])
  onMouseUp(e: any): void {
    // The mouse button is released, so this means the end of drawing the signature
    this.isDrawing = false;
  }

  onMouseMove(e: any): void {
    if (this.isDrawing) { // if we're not drawing we need to ignore the events
      const coords = this.relativeCoords(e);
      this.context.lineTo(coords.x, coords.y);
      this.context.stroke();
      this.drawn = true;
    }
  }

  clearSignature(): void {
    this.signatureImg = undefined;
    this.context.clearRect(0, 0, this.sigPadElement.width, this.sigPadElement.height);
    this.context.beginPath();
  }

  saveSignature(): void {
    if(!this.drawn)
    {
      this.toastService.show("Necesitas dibujar tu firma con el dedo o el ratón.",
            "¡Ups!", 
            { status: 'danger', destroyByClick: true, duration: 5000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
           );

      return;
    }

    this.signatureImg = this.sigPadElement.toDataURL('image/png');  
    //this.signatureSaved.emit(this.signatureImg);
    this.signPDF();
  }

  private relativeCoords(event: any): { x: number, y: number } {
    const bounds = event.target.getBoundingClientRect();
    const cords = {
      clientX: event.clientX || event.changedTouches[0].clientX,
      clientY: event.clientY || event.changedTouches[0].clientY
    };
    const x = cords.clientX - bounds.left;
    const y = cords.clientY - bounds.top;
    return {x, y};
  }

  goBack() {
    this.ref.close('close');
  }


  async signPDF()
  {
    const url = this.urlpdf;
    const existingPdfBytes = await fetch(url).then(res => res.arrayBuffer());
    const pdfDoc = await PDFDocument.load(existingPdfBytes);
    const helveticaFont = await pdfDoc.embedFont(StandardFonts.Helvetica);

    const page = pdfDoc.addPage()


    const pngImage = await pdfDoc.embedPng(this.signatureImg);
    const pngDims = pngImage.scale(0.75)

    page.drawText('Firmado por ' + this.getFullname() + '\n' + this.date2Text(), {
      x: page.getWidth() / 2 - pngDims.width / 2 - 60,
      y: page.getHeight() / 2 - pngDims.height,
      size: 14,
      font: helveticaFont,
      color: rgb(0, 0, 0),
    })

    page.drawImage(pngImage, {
      x: page.getWidth() / 2 - pngDims.width / 2 - 60,
      y: page.getHeight() / 2 - pngDims.height + 40,
      width: pngDims.width,
      height: pngDims.height,
    })


    // Serialize the PDFDocument to bytes (a Uint8Array)
    const pdfBytes = await pdfDoc.save()

    // Trigger the browser to download the PDF document
    const file = new Blob([pdfBytes], {type: 'application/pdf'});

    
    this.mediaService.uploadFile(new File([file], "documento_firmado.pdf")).subscribe(
      res => {
          /*this.toastService.show("Documento firmado correctamente",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 5000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );*/
          saveAs(file, "documento_firmado.pdf");
          this.ref.close(res);
      },
      error => {
        if(error.status && error.status == 200)
        {
          /*this.toastService.show("Documento firmado correctamente",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 5000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );*/
          saveAs(file, "documento_firmado.pdf");
          this.ref.close(error.error.text);
        }
        else
        {
          console.error("uploadFile():"+JSON.stringify(error));
          this.toastService.show("No se ha podido firmar el fichero.",
            "¡Ups!", 
            { status: 'danger', destroyByClick: true, duration: 5000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
           );
        }
      }
    );

  }


  
  date2Text()
  {
    let thedate : Date = new Date();
    let daysOfWeek = [ 'domingo', 'lunes', 'martes', 'miércoles', 'jueves', 'viernes', 'sábado' ];
    const dayOfWeek = daysOfWeek[thedate.getDay()];
    return 'el ' + daysOfWeek[thedate.getDay()] + " " + this.completeZeros(thedate.getDate()) + "/" + this.completeZeros(thedate.getMonth()+1) + "/" + (thedate.getFullYear()+"") + " a las " +  this.completeZeros(thedate.getHours()) + ":" + this.completeZeros(thedate.getMinutes()) + " h.";
  }

  completeZeros(x : number) : string
  {
    if(x<=9) return "0" + x;
    else return ""+x;
    
  }

  
  getFullname() {
    const data: LoginResponse = this.localStorageService.getObject('logged');
    return data.fullname + (data.dni?', con DNI/NIE: ' + data.dni:'');
  }

}