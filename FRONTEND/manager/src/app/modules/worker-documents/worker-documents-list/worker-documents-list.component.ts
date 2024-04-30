import { Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import { PatientDTO, parseDataExport, ActionDTO, FeedingDTO, rolName, DocDTO } from 'src/app/shared/models/models';
import { UsersService } from 'src/app/core/services/users.service';
import { PdfService } from 'src/app/core/services/pdf-service.service';
import { ExcelService } from 'src/app/core/services/excel-service.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FeedingsService } from 'src/app/core/services/feedings.service';
import { flatpickrFactory } from '../../calendar/mycalendar.module'; 
import { DocsService } from 'src/app/core/services/docs.service';
import { Subscription, finalize } from 'rxjs';
import { MediaService } from 'src/app/core/services/media.service';
import { HttpEventType } from '@angular/common/http';


@Component({
  selector: 'app-worker-documents-list',
  templateUrl: './worker-documents-list.component.html',
  styleUrls: ['./worker-documents-list.component.scss']
})
export class WorkerDocumentsListComponent implements OnInit {

  

  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;

  //PARÁMETROS LISTADO
  dayfrom : Date = null; 
  dayto : Date = null;
  theText : string = '';

  page : number = 0;
  size : number = 4;
  totalPages : number = 0;

  allRoles : string[] = [ "RELATIVE", "WORKER", "TRANSPORT", "ADMIN", "CLEANING", "NURSING" , "NURSING_ASSISTANT" , "LEGIONELLA_CONTROL" , "KITCHEN" , "MONITOR" , "SOCIAL_WORKER" , "PSYCHOLOGIST" , "MANAGER" , "PHYSIOTHERAPIST", "OCCUPATIONAL_THERAPIST", "OPERATOR_EXTRA_1" ];

  requiredFileType:string  = ".png,.jpg,.jpeg,.webp,.gif,.pdf,.doc,.docx,.xsl,.xslx";
  fileName = '';
  uploadProgress:number;
  uploadSub: Subscription;

  theDoc : DocDTO = {
    id: '',
    idworker: '',
    worker_fullname: '',
    title: '',
    description: '',
    url: '',
    dayfrom: null,
    dayto: null,
    roles: [],
    created: undefined
  };
  
  docs: any[]  = []; // amy => formato del listado
  docsObjects: DocDTO[]  = [];

  loadingExcel : boolean = false;
  loadingPDF : boolean = false;
  isProcessing : boolean = true;

  exportData: any[];

  actions : ActionDTO[] = [];

  constructor(
    public toastService: NbToastrService,
    private mediaService : MediaService,
    private pdfService : PdfService,
    private excelService : ExcelService,
    private authService : AuthService,
    private modal: NgbModal, 
    private docsService : DocsService

  ) {
   }

  ngOnInit(): void {
    flatpickrFactory();
    this.dayfrom = null;
    this.dayto = null;
    if(!this.canModify()) this.actions = [{action: 'show', text: 'Abrir documento'}];
    if(this.canModify()) this.actions = [{action: 'show', text: 'Abrir documento'}, {action: 'edit', text: 'Modificar documento'}, {action: 'delete', text: 'Eliminar documento'}];
    this.getDocs(0);
  }


  roleName(therole) { return rolName(therole); }
  


  getDocs(page :number) 
  {
    this.isProcessing = true;
    //if(!this.dayfrom) this.dayfrom = null;
    //if(!this.dayto) this.dayto = null;
    this.page = page
    this.docsService.getDocs(this.page, this.size, this.dayfrom, this.dayto, this.theText).subscribe(
      res => {
        this.isProcessing = false;
        this.docs = res.content.map(item => { return {id: item.id, values: [item.title, item.description, this.date2Text1(item.dayfrom), this.date2Text1(item.dayto), item.worker_fullname ] }; });
        this.docsObjects = res.content;

        this.totalPages = res.totalPages;
      },
      error => {
        this.isProcessing = false;
        console.error("getDocs():"+JSON.stringify(error));
        this.toastService.show("No se han podido obtener los documentos.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }

  

  
  getExportData(format: string) 
  {
    if(format === 'excel') this.loadingExcel = true;
    else if(format === 'pdf') this.loadingPDF = true;

    if(!this.dayfrom) this.dayfrom = new Date();
    if(!this.dayto) this.dayto = new Date();

    this.docsService.getDocs(0, 100000000, this.dayfrom, this.dayto, this.theText).subscribe(
      res => {
        const header = {};
        const keys = ['Título:', 'Descripción:',  'Publicado desde:', 'Publicado hasta:', 'Subido por:'];
        const fields = ['patient_fullname', 'day', 'daymeal', 'dish', 'result', 'indications', 'incidences', 'worker_fullname'];
        fields.forEach((key, i) => header[key] = keys[i]);
        this.exportData = res && res.content ? res.content.map(item => [item.title, item.description, this.date2Text1(item.dayfrom), this.date2Text1(item.dayto), item.worker_fullname ]) : null;
        const final = parseDataExport(fields, this.exportData);
        const title = 'Documentos';

        if (format === 'excel') 
        {
          this.excelService.exportAsExcelFile(title, [header].concat(final), title, fields);
          this.loadingExcel = false;
          this.loadingPDF = false;
        } 
        else 
        {
          const minFields = ['20%','20%','20%','20%','20%'];
          const keystranslate = keys.map(item => item);
          this.pdfService.exportDataToPDF(title, keystranslate, fields, title, final, minFields);
          this.loadingPDF = false;
        }
      },
      _ => {
        this.loadingExcel = false;
        this.loadingPDF = false;
      }
    );
  }


  filter(page: number) {
    this.getDocs(page);
  }


  setPage(event) {
    this.page = event;
    this.getDocs(this.page);
  }

  action(event) 
  {
    if (event && event[0] === 'edit') 
    {
      const selected = { ...this.docsObjects.find(item => item.id === event[1])};
      this.openAddDocumentModal(selected);
    } 
    else if (event && event[0] === 'delete') 
    {
      this.deleteDoc(event[1]);
    }
    else if (event && event[0] === 'show') 
    {
      const selected =  {...this.docsObjects.find(item => item.id === event[1])};
      window.open(selected.url);
    } 

  }


  canModify() : boolean
  {
    return this.authService.isManager() || this.authService.isAdmin();
  }



  openAddDocumentModal(selected? : DocDTO)
  {
    console.warn(JSON.stringify(selected));
    if(!selected)
    {
      this.fileName = undefined;
      this.theDoc.id =''; 
      this.theDoc.url =''; 
      this.theDoc.title =''; 
      this.theDoc.description ='';
      this.theDoc.dayfrom = new Date();
      this.theDoc.dayto = null;
      this.theDoc.roles = [ "WORKER" ];
    }
    else
    {
      this.theDoc = selected;
      this.theDoc.dayfrom = this.localDateTime2Date(this.theDoc.dayfrom);
      this.theDoc.dayto = this.localDateTime2Date(this.theDoc.dayto);
    }



    this.modal.open(this.modalContent, { size: 'lg' });
  }

  saveDoc()
  {

    if(this.theDoc.url == '') 
    {
      this.toastService.show("Debes seleccionar el documento a subir primero.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }


    if(this.theDoc.title == '') 
    {
      this.toastService.show("Debes indicar un título para el documento.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }
   

    this.docsService.saveDoc(this.theDoc).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Documento añadido correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getDocs(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("saveDoc():"+JSON.stringify(error));
        this.toastService.show("No se ha podido añadir el documento.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }


  deleteDoc(id : string)
  {
    this.docsService.deleteDoc(id).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Documento eliminado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getDocs(this.page);
      },
      error => {
        this.isProcessing = false;
        console.error("deleteFeeding():"+JSON.stringify(error));
        this.toastService.show("No se ha podido eliminar el documento.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }



  


async onFileSelected(event) {

  const file:File = event.target.files[0];

  if (file) 
  {
    const upload$ = this.mediaService.uploadFile("document", file).pipe(finalize(() => this.reset())
  )
  ;


  this.uploadSub = upload$.subscribe(event => {
        if (event.type == HttpEventType.UploadProgress) 
        {
          this.uploadProgress = Math.round(100 * (event.loaded / event.total));
        }
        else if(event.type == HttpEventType.Response && event.status == 200 )
        {
          if(event.url && event.url.startsWith("https://") )
          {
            this.theDoc.url = event.url;
          }
          else
            console.error("onFileSelected1():"+JSON.stringify(event));
        }
        else if(event.type == HttpEventType.Response && (event.status == 403 || event.status == 500) ) //mod_evasive
        {
          console.error("onFileSelected2():"+JSON.stringify(event));
          this.toastService.show("No se ha podido subir el fichero.",
            "¡Ups!", 
            { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        }
        else 
        {
          if(event.url && event.url.startsWith("https://") ) 
            this.theDoc.url = event.url;
          else
            console.error("onFileSelected3():"+JSON.stringify(event));
        }
      }
    )
  }
}


cancelUpload() {
this.uploadSub.unsubscribe();
this.reset();
}

reset() {
this.uploadProgress = null;
this.uploadSub = null;

}


  disabledDay(date) {
    return false;
    /*
    date.setDate(date.getDate());
   return date > new Date();
   */
  }

  
  //[2024,3,1,13,0,...] --> Date
  localDateTime2Date(thedate : any) : Date
  {
    if(!thedate || thedate.length==0) return null;
    let sdate = '';
    if(thedate[0]) sdate += thedate[0];
    if(thedate[1]) sdate += ('-' + this.completeZeros(thedate[1]));
    if(thedate[2]) sdate += ('-' + this.completeZeros(thedate[2]));
    if(thedate[3] || thedate[3]==0) sdate += ('T' + this.completeZeros(thedate[3]));
    if(thedate[4] || thedate[4]==0) sdate += (':' + this.completeZeros(thedate[4]));
    if(thedate[5] || thedate[5]==0) sdate += (':' + this.completeZeros(thedate[5]));
    if(thedate[6] || thedate[6]==0) sdate += ('.' + thedate[6]);

    const result : Date = new Date(Date.parse(sdate));
    if(result.getFullYear()==2100) return null;
    return result;
  }

  date2Text(thedate : any)
  {
    if(!thedate) return '';
    if(thedate instanceof Date) return this.date2Text2(thedate);
    else return this.date2Text1(thedate);
  }

  date2Text1(thelocaldatetime : number[])
  {
    if(!thelocaldatetime) return '';
    let thedate : Date = this.localDateTime2Date(thelocaldatetime);
    if(!thedate) return '';
    let daysOfWeek = [ 'domingo', 'lunes', 'martes', 'miércoles', 'jueves', 'viernes', 'sábado' ];
    const dayOfWeek = daysOfWeek[thedate.getDay()];
    
    if(thedate.getFullYear()==2100) return 'No expira';

    return 'El ' + daysOfWeek[thedate.getDay()] + " " + this.completeZeros(thedate.getDate()) + "/" + this.completeZeros(thedate.getMonth()+1) + "/" + (thedate.getFullYear()+"");
  }


  date2Text2(thedate : Date)
  {
    if(!thedate) return '';
    let daysOfWeek = [ 'domingo', 'lunes', 'martes', 'miércoles', 'jueves', 'viernes', 'sábado' ];
    const dayOfWeek = daysOfWeek[thedate.getDay()];
    //return 'el ' + daysOfWeek[thedate.getDay()] + " " + this.completeZeros(thedate.getDate()) + "/" + this.completeZeros(thedate.getMonth()+1) + "/" + (thedate.getFullYear()+"") + " a las " +  this.completeZeros(thedate.getHours()) + ":" + this.completeZeros(thedate.getMinutes()) + " h.";
    return 'el ' + daysOfWeek[thedate.getDay()] + " " + this.completeZeros(thedate.getDate()) + "/" + this.completeZeros(thedate.getMonth()+1) + "/" + (thedate.getFullYear()+"");
  }




  completeZeros(x : number) : string
  {
    if(x<=9) return "0" + x;
    else return ""+x;
  }



  

}
