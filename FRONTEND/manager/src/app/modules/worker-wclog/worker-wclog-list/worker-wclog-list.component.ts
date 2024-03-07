import { Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import { PatientDTO, parseDataExport, ActionDTO, FeedingDTO, LegionellaLogDTO, WCLogDTO } from 'src/app/shared/models/models';
import { UsersService } from 'src/app/core/services/users.service';
import { PdfService } from 'src/app/core/services/pdf-service.service';
import { ExcelService } from 'src/app/core/services/excel-service.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FeedingsService } from 'src/app/core/services/feedings.service';
import { flatpickrFactory } from '../../calendar/mycalendar.module'; 
import { LegionellalogsService } from 'src/app/core/services/legionellalogs.service';
import { WclogsService } from 'src/app/core/services/wclogs.service';



@Component({
  selector: 'app-worker-wclog-list',
  templateUrl: './worker-wclog-list.component.html',
  styleUrls: ['./worker-wclog-list.component.scss']
})
export class WorkerWclogListComponent implements OnInit 
{


  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;

  //PARÁMETROS LISTADO
  dayfrom : Date = new Date();
  dayto : Date = new Date();

  page : number = 0;
  size : number = 4;
  totalPages : number = 0;


  theWC : WCLogDTO = {
    id: '',
    idworker: '',
    worker_fullname: '',
    day: new Date(),
    point: '',
    hour: ''
  };
  
  wclogs: any[]  = []; // amy => formato del listado
  wclogsObjects: WCLogDTO[]  = [];

  loadingExcel : boolean = false;
  loadingPDF : boolean = false;
  isProcessing : boolean = true;

  exportData: any[];

  actions : ActionDTO[] = [];

  constructor(
    public toastService: NbToastrService,
    private usersService : UsersService,
    private pdfService : PdfService,
    private excelService : ExcelService,
    private authService : AuthService,
    private modal: NgbModal, 
    private wclogsService : WclogsService

  ) { }

  ngOnInit(): void {
    flatpickrFactory();
    this.getWCLogs(0);
    if(this.canModify()) this.actions = [{action: 'edit', text: 'Modificar registro de limpieza'},{action: 'delete', text: 'Eliminar registro de limpieza'}];
  }




  getWCLogs(page :number) 
  {
    this.wclogs = [];
    this.wclogsObjects = [];
    this.isProcessing = true;
    if(!this.dayfrom) this.dayfrom = new Date();
    if(!this.dayto) this.dayto = new Date();
    this.page = page
    this.wclogsService.getWCLogs(this.page, this.size, this.dayfrom, this.dayto).subscribe(
      res => {
        this.isProcessing = false;
        this.wclogs = res.content.map(item => { return {id: item.id, values: [this.date2Text1(item.day), item.hour, item.point, item.worker_fullname] }; });
        this.wclogsObjects = res.content;

        this.totalPages = res.totalPages;
      },
      error => {
        this.isProcessing = false;
        console.error("getWCLogs():"+JSON.stringify(error));
        this.toastService.show("No se ha podido obtener la información del registro de limpieza.",
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

    this.wclogsService.getWCLogs(0, 100000000, this.dayfrom, this.dayto).subscribe(
      res => {
        const header = {};
        const keys = ['Día:', 'Hora', 'Punto:', 'Limpiado por:'];
        const fields = ['day', 'hour', 'point', 'worker_fullname'];
        fields.forEach((key, i) => header[key] = keys[i]);
        this.exportData = res && res.content ? res.content.map(item => [this.date2Text1(item.day), item.hour, item.point, item.worker_fullname]) : null;
        const final = parseDataExport(fields, this.exportData);
        const title = 'Registro de Limpieza';

        if (format === 'excel') 
        {
          this.excelService.exportAsExcelFile(title, [header].concat(final), title, fields);
          this.loadingExcel = false;
          this.loadingPDF = false;
        } 
        else 
        {
          const minFields = ['20%','20%','20%','40%'];
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
    this.getWCLogs(page);
  }


  setPage(event) {
    this.page = event;
    this.getWCLogs(this.page);
  }

  action(event) 
  {
    if (event && event[0] === 'edit') 
    {
      const selected = this.wclogsObjects.find(item => item.id === event[1]);
      this.theWC = selected;

      this.openRegisterWCLogModal();
    } 
    else if (event && event[0] === 'delete') 
    {
      this.deleteWCLog(event[1]);
    } 

    
  }


  canModify() : boolean
  {
    return this.authService.isCleaning();
  }



  openRegisterWCLogModal()
  {
    this.modal.open(this.modalContent, { size: 'lg' });
  }

  registerWCLog()
  {


    if(!this.theWC.point || this.theWC.point.trim() == '')
    {
      this.toastService.show("Debes indicar el punto.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }



    if(!this.theWC.hour || this.theWC.hour.trim() == '')
    {
      this.toastService.show("Debes indicar la hora.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }




    this.wclogsService.registerWCLog(this.theWC).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Registro de limpieza realizado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getWCLogs(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("registerWCLog():"+JSON.stringify(error));
        this.toastService.show("No se ha podido realizar el registro de alimentación.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }


  deleteWCLog(id : string)
  {
    this.wclogsService.deleteWCLog(id).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Registro de limpieza eliminado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getWCLogs(this.page);
      },
      error => {
        this.isProcessing = false;
        console.error("deleteWCLog():"+JSON.stringify(error));
        this.toastService.show("No se ha podido eliminar el registro de limpieza.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }




  disabledDay(date) {
    date.setDate(date.getDate());
   return date > new Date();
  }

  
  //[2024,3,1,13,0,...] --> Date
  localDateTime2Date(thedate : number[]) : Date
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
    return new Date(Date.parse(sdate));
  }

  date2Text(thedate : any)
  {
    if(thedate instanceof Date) return this.date2Text2(thedate);
    else return this.date2Text1(thedate);
  }

  date2Text1(thelocaldatetime : number[])
  {
    let thedate : Date = this.localDateTime2Date(thelocaldatetime);
    if(!thedate) return '';
    let daysOfWeek = [ 'domingo', 'lunes', 'martes', 'miércoles', 'jueves', 'viernes', 'sábado' ];
    const dayOfWeek = daysOfWeek[thedate.getDay()];
    return 'El ' + daysOfWeek[thedate.getDay()] + " " + this.completeZeros(thedate.getDate()) + "/" + this.completeZeros(thedate.getMonth()+1) + "/" + (thedate.getFullYear()+"");
  }


  date2Text2(thedate : Date)
  {
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
