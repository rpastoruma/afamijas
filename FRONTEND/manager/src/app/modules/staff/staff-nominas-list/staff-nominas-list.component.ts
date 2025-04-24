import { Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import { parseDataExport, ActionDTO, rolName, ReceiptDTO, MemberDTO, NominaDTO, WorkerDTO } from 'src/app/shared/models/models';
import { UsersService } from 'src/app/core/services/users.service';
import { PdfService } from 'src/app/core/services/pdf-service.service';
import { ExcelService } from 'src/app/core/services/excel-service.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { flatpickrFactory } from '../../calendar/mycalendar.module'; 
import { Subscription, finalize } from 'rxjs';
import { MediaService } from 'src/app/core/services/media.service';
import { HttpEventType } from '@angular/common/http';
import { NominasService } from 'src/app/core/services/nominas.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-staff-nominas-list',
  templateUrl: './staff-nominas-list.component.html',
  styleUrls: ['./staff-nominas-list.component.scss']
})
export class StaffNominasListComponent implements OnInit {

  

  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;

  //PARÁMETROS LISTADO
  dayfrom : Date = null; 
  dayto : Date = null;
  idworker : string = '';

  page : number = 0;
  size : number = 4;
  totalPages : number = 0;

  
  requiredFileType:string  = ".png,.jpg,.jpeg,.webp,.gif,.pdf,.doc,.docx,.xsl,.xslx";
  fileName = '';
  uploadProgress:number;
  uploadSub: Subscription;

  theNomina : NominaDTO = {
    id: '',
    idworker: '',
    worker_fullname: '',
    url: '',
    duedate: undefined,
    status: 'A'
  };

  allWorkers : WorkerDTO[] = [];
  
  nominas: any[]  = []; // any => formato del listado
  nominasObjects: NominaDTO[]  = [];

  loadingExcel : boolean = false;
  loadingPDF : boolean = false;
  isProcessing : boolean = true;

  exportData: any[];

  actions : ActionDTO[] = [];

  paramsuscription : Subscription;
  idworker_param : string;

  constructor(
    public toastService: NbToastrService,
    private mediaService : MediaService,
    private pdfService : PdfService,
    private excelService : ExcelService,
    private authService : AuthService,
    private modal: NgbModal, 
    private nominasService : NominasService,
    private usersService : UsersService,
    private router: Router,
    private route: ActivatedRoute

  ) {
   }

  ngOnInit(): void {
    flatpickrFactory();
    this.dayfrom = null;
    this.dayto = null;
    if(!this.canModify()) this.actions = [{action: 'show', text: 'Ver nómina'}];
    if(this.canModify()) this.actions = [{action: 'show', text: 'Ver nómina'}, {action: 'edit', text: 'Modificar nómina'}, {action: 'delete', text: 'Eliminar nómina'}];
    
    
    
    this.paramsuscription = this.route.queryParams.subscribe(params => {
      console.log(params)
      this.idworker_param = params['idworker'];
      this.getAllWorkers(this.idworker_param);
   });

  }


  roleName(therole) { return rolName(therole); }
  

  
  getAllWorkers(idworkerparam :string)
  {
      this.isProcessing = true;
      this.usersService.getAllWorkers().subscribe(
        res => {
          this.allWorkers = res;
          if(this.allWorkers && this.allWorkers.length>0) 
          {
            if(idworkerparam) this.idworker = idworkerparam;
            this.getNominas(0);
          }
          else
            this.isProcessing = false;
        },
        error => 
        {
          console.error("getAllWorkers():"+JSON.stringify(error));
          this.toastService.show("No se pueden obtener los trabajadores.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
        }
      );
  }


  getNominas(page :number) 
  {
    this.isProcessing = true;
    //if(!this.dayfrom) this.dayfrom = null;
    //if(!this.dayto) this.dayto = null;
    this.page = page
    this.nominasService.getNominas(this.page, this.size, this.idworker, this.dayfrom, this.dayto).subscribe(
      res => {
        this.isProcessing = false;
        this.nominas = res.content.map(item => { return {id: item.id, values: [item.worker_fullname, this.date2Text1(item.duedate), item.url ] }; });
        this.nominasObjects = res.content;

        this.totalPages = res.totalPages;
      },
      error => {
        this.isProcessing = false;
        console.error("getNominas():"+JSON.stringify(error));
        this.toastService.show("No se han podido obtener las nóminas.",
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

    this.nominasService.getNominas(0, 100000000, this.idworker, this.dayfrom, this.dayto).subscribe(
      res => {
        const header = {};
        const keys = ['Socio:', 'Fecha:'];
        const fields = ['worker_fullname', 'paiddate'];
        fields.forEach((key, i) => header[key] = keys[i]);
        this.exportData = res && res.content ? res.content.map(item => [item.worker_fullname, this.date2Text1(item.duedate) ]) : null;
        const final = parseDataExport(fields, this.exportData);
        const title = 'Nóminas';

        if (format === 'excel') 
        {
          this.excelService.exportAsExcelFile(title, [header].concat(final), title, fields);
          this.loadingExcel = false;
          this.loadingPDF = false;
        } 
        else 
        {
          const minFields = ['80%','20%'];
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
    this.getNominas(page);
  }


  setPage(event) {
    this.page = event;
    this.getNominas(this.page);
  }

  action(event) 
  {
    if (event && event[0] === 'edit') 
    {
      const selected = { ...this.nominasObjects.find(item => item.id === event[1])};
      this.openAddNominaModal(selected);
    } 
    else if (event && event[0] === 'delete') 
    {
      this.deleteNomina(event[1]);
    }
    else if (event && event[0] === 'show') 
    {
      const selected =  {...this.nominasObjects.find(item => item.id === event[1])};
      window.open(selected.url);
    } 

  }


  canModify() : boolean
  {
    return this.authService.isManager() || this.authService.isAdmin();
  }



  openAddNominaModal(selected? : NominaDTO)
  {
    console.warn(JSON.stringify(selected));
    if(!selected)
    {
      this.fileName = undefined;
      this.theNomina.id =''; 
      this.theNomina.url =''; 
      this.theNomina.duedate = new Date();
      this.theNomina.idworker = this.idworker;
    }
    else
    {
      this.theNomina = selected;
      this.theNomina.duedate = this.localDateTime2Date(this.theNomina.duedate);
    }



    this.modal.open(this.modalContent, { size: 'lg' });
  }

  saveNomina()
  {

    
    if(this.theNomina.idworker == '') 
      {
        this.toastService.show("Debes seleccionar el trabajador primero.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
        );
        return;
      }

    if(this.theNomina.url == '') 
    {
      this.toastService.show("Debes seleccionar la nómina a subir.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }


    this.nominasService.saveNomina(this.theNomina).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Nómina grabada correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getNominas(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("saveReceipt():"+JSON.stringify(error));
        this.toastService.show("No se ha podido grabar la nómina.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }


  deleteNomina(id : string)
  {
    this.nominasService.deleteNomina(id).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Nómina eliminada correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getNominas(this.page);
      },
      error => {
        this.isProcessing = false;
        console.error("deleteFeeding():"+JSON.stringify(error));
        this.toastService.show("No se ha podido eliminar la nómina.",
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
    const upload$ = this.mediaService.uploadFile("nomina", file).pipe(finalize(() => this.reset())
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
            this.theNomina.url = event.url;
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
            this.theNomina.url = event.url;
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
