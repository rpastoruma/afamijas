import { Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import { parseDataExport, ActionDTO, rolName, WorkerDTO, ContratoDTO } from 'src/app/shared/models/models';
import { UsersService } from 'src/app/core/services/users.service';
import { PdfService } from 'src/app/core/services/pdf-service.service';
import { ExcelService } from 'src/app/core/services/excel-service.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { flatpickrFactory } from '../../calendar/mycalendar.module'; 
import { Subscription, finalize } from 'rxjs';
import { MediaService } from 'src/app/core/services/media.service';
import { HttpEventType } from '@angular/common/http';
import { ContratosService } from 'src/app/core/services/contratos.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-staff-contratos-list',
  templateUrl: './staff-contratos-list.component.html',
  styleUrls: ['./staff-contratos-list.component.scss']
})
export class StaffContratosListComponent implements OnInit {

  

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

theContrato: ContratoDTO = {
  id: '',
  idworker: '',
  worker_fullname: '',
  url: '',
  startdate: undefined,
  enddate: undefined,
  status: 'A'
};
  allWorkers : WorkerDTO[] = [];
  
  contratos: any[]  = []; // any => formato del listado
  contratosObjects: ContratoDTO[]  = [];

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
    private contratosService : ContratosService,
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
            this.getContratos(0);
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


getContratos(page: number) {
  this.isProcessing = true;
  this.page = page;

  this.contratosService.getContratos(
    this.page,
    this.size,
    this.idworker,
    this.dayfrom,
    this.dayto
  ).subscribe(
    res => {
      this.isProcessing = false;

      this.contratos = res.content.map(item => ({
        id: item.id,
        values: [
          item.worker_fullname,
          this.date2Text1(item.startdate),
          this.date2Text1(item.enddate)
        ]
      }));

      this.contratosObjects = res.content;
      this.totalPages = res.totalPages;
    },
    error => {
      this.isProcessing = false;
      console.error(error);
    }
  );
}

  

  
  getExportData(format: string) 
  {
    if(format === 'excel') this.loadingExcel = true;
    else if(format === 'pdf') this.loadingPDF = true;

    if(!this.dayfrom) this.dayfrom = new Date();
    if(!this.dayto) this.dayto = new Date();

    this.contratosService.getContratos(0, 100000000, this.idworker, this.dayfrom, this.dayto).subscribe(
      res => {
        const header = {};
        const keys = ['Trabajador:', 'Inicio:', 'Fin:'];
        const fields = ['worker_fullname', 'startdate', 'enddate'];
        fields.forEach((key, i) => header[key] = keys[i]);
        this.exportData = res && res.content ? res.content.map(item => [item.worker_fullname, this.date2Text1(item.startdate), this.date2Text1(item.enddate) ]) : null;
        const final = parseDataExport(fields, this.exportData);
        const title = 'Contratos';

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
    this.getContratos(page);
  }


  setPage(event) {
    this.page = event;
    this.getContratos(this.page);
  }

  action(event) 
  {
    if (event && event[0] === 'edit') 
    {
      const selected = { ...this.contratosObjects.find(item => item.id === event[1])};
      this.openAddContratoModal(selected);
    } 
    else if (event && event[0] === 'delete') 
    {
      this.deleteContrato(event[1]);
    }
    else if (event && event[0] === 'show') 
    {
      const selected =  {...this.contratosObjects.find(item => item.id === event[1])};
      window.open(selected.url);
    } 

  }


  canModify() : boolean
  {
    return this.authService.isManager() || this.authService.isAdmin()  || this.authService.isSocialWorker();
  }



  openAddContratoModal(selected? : ContratoDTO)
  {
    console.warn(JSON.stringify(selected));
    if(!selected)
    {
      this.fileName = undefined;
      this.theContrato.id =''; 
      this.theContrato.url =''; 
      this.theContrato.startdate = new Date();
      this.theContrato.enddate = new Date();
      this.theContrato.idworker = this.idworker;
    }
    else
    {
      this.theContrato = selected;
      this.theContrato.startdate = this.localDateTime2Date(this.theContrato.startdate);
      this.theContrato.enddate = this.localDateTime2Date(this.theContrato.enddate );
    }



    this.modal.open(this.modalContent, { size: 'lg' });
  }

  saveContrato()
  {

    
    if(this.theContrato.idworker == '') 
      {
        this.toastService.show("Debes seleccionar el trabajador primero.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
        );
        return;
      }

    if(this.theContrato.url == '') 
    {
      this.toastService.show("Debes seleccionar la nómina a subir.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }


    this.contratosService.saveContrato(this.theContrato).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Contrato grabado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getContratos(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("saveReceipt():"+JSON.stringify(error));
        this.toastService.show("No se ha podido grabar el contrato.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }


  deleteContrato(id : string)
  {
    this.contratosService.deleteContrato(id).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Contrato eliminado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getContratos(this.page);
      },
      error => {
        this.isProcessing = false;
        console.error("deleteFeeding():"+JSON.stringify(error));
        this.toastService.show("No se ha podido eliminar el contrato.",
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
    const upload$ = this.mediaService.uploadFile("contrato", file).pipe(finalize(() => this.reset())
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
            this.theContrato.url = event.url;
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
            this.theContrato.url = event.url;
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
