import { Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import { PatientDTO, parseDataExport, ActionDTO, FeedingDTO, LegionellaLogDTO } from 'src/app/shared/models/models';
import { UsersService } from 'src/app/core/services/users.service';
import { PdfService } from 'src/app/core/services/pdf-service.service';
import { ExcelService } from 'src/app/core/services/excel-service.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FeedingsService } from 'src/app/core/services/feedings.service';
import { flatpickrFactory } from '../../calendar/mycalendar.module'; 
import { LegionellalogsService } from 'src/app/core/services/legionellalogs.service';



@Component({
  selector: 'app-worker-legionella-list',
  templateUrl: './worker-legionella-list.component.html',
  styleUrls: ['./worker-legionella-list.component.scss']
})
export class WorkerLegionellaListComponent implements OnInit 
{


  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;

  //PARÁMETROS LISTADO
  dayfrom : Date = new Date();
  dayto : Date = new Date();

  page : number = 0;
  size : number = 4;
  totalPages : number = 0;


  theLegionellaLog : LegionellaLogDTO = {
    id: '',
    idworker: '',
    worker_fullname: '',
    day: new Date(),
    point: '',
    value: 0.5,
    temperature: 60,
    isOk: true
  };
  
  legionellalogs: any[]  = []; // amy => formato del listado
  legionellalogsObjects: LegionellaLogDTO[]  = [];

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
    private legionelaLogsService : LegionellalogsService

  ) { }

  ngOnInit(): void {
    flatpickrFactory();
    this.getLegionellaLogs(0);
    if(this.canModify()) this.actions = [{action: 'edit', text: 'Modificar registro de Legionella'},{action: 'delete', text: 'Eliminar registro de Legionella'}];
  }




  getLegionellaLogs(page :number) 
  {
    this.legionellalogs = [];
    this.legionellalogsObjects = [];
    this.isProcessing = true;
    if(!this.dayfrom) this.dayfrom = new Date();
    if(!this.dayto) this.dayto = new Date();
    this.page = page
    this.legionelaLogsService.getLegionellaLogs(this.page, this.size, this.dayfrom, this.dayto).subscribe(
      res => {
        this.isProcessing = false;
        this.legionellalogs = res.content.map(item => { return {id: item.id, values: [this.date2Text1(item.day), item.point, this.getVal(item.value), this.getTemp(item.temperature), item.worker_fullname] }; });
        this.legionellalogsObjects = res.content;

        this.totalPages = res.totalPages;
      },
      error => {
        this.isProcessing = false;
        console.error("getLegionellaLogs():"+JSON.stringify(error));
        this.toastService.show("No se ha podido obtener la información del registro de Legionella.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }

  
  getTemp(temp : number)
  {
    const stemp = temp>0?'+'+temp:temp;
    if(temp>=60) return "<div class=\"green\">" + stemp + " ºC</div>";
    else return "<div class=\"red\">" + stemp + " ºC</div>";
  }

  getVal(val : number)
  {
    if(val>=0.2 && val <= 1.0) return "<div class=\"green\">" + val + " ºC</div>";
    else return "<div class=\"red\">" + val + " mg/l</div>";
  }

  

  
  getExportData(format: string) 
  {
    if(format === 'excel') this.loadingExcel = true;
    else if(format === 'pdf') this.loadingPDF = true;

    if(!this.dayfrom) this.dayfrom = new Date();
    if(!this.dayto) this.dayto = new Date();

    this.legionelaLogsService.getLegionellaLogs(0, 100000000, this.dayfrom, this.dayto).subscribe(
      res => {
        const header = {};
        const keys = ['Día:', 'Punto:', 'C.R.L. (mg/l):', 'Temperatura (ºC):', 'Registrado por:'];
        const fields = ['day', 'point', 'value', 'temperature', 'worker_fullname'];
        fields.forEach((key, i) => header[key] = keys[i]);
        this.exportData = res && res.content ? res.content.map(item => [this.date2Text1(item.day), item.point, item.value, item.temperature, item.worker_fullname]) : null;
        const final = parseDataExport(fields, this.exportData);
        const title = 'Registro de Legionella';

        if (format === 'excel') 
        {
          this.excelService.exportAsExcelFile(title, [header].concat(final), title, fields);
          this.loadingExcel = false;
          this.loadingPDF = false;
        } 
        else 
        {
          const minFields = ['20%','30%','10%','10%','30%'];
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
    this.getLegionellaLogs(page);
  }


  setPage(event) {
    this.page = event;
    this.getLegionellaLogs(this.page);
  }

  action(event) 
  {
    if (event && event[0] === 'edit') 
    {
      const selected = this.legionellalogsObjects.find(item => item.id === event[1]);
      this.theLegionellaLog = selected;

      this.openRegisterLegionellaLogModal();
    } 
    else if (event && event[0] === 'delete') 
    {
      this.deleteLegionellaLog(event[1]);
    }
  }


  canModify() : boolean
  {
    return this.authService.isLegionellaLog();
  }



  openRegisterLegionellaLogModal()
  {
    this.modal.open(this.modalContent, { size: 'lg' });
  }

  registerLegionellaLog()
  {
    if(!this.theLegionellaLog.day) this.theLegionellaLog.day = new Date();

    if(!this.theLegionellaLog.point || this.theLegionellaLog.point.trim() == '')
    {
      this.toastService.show("Debes indicar el punto.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }




    this.legionelaLogsService.registerLegionellaLog(this.theLegionellaLog).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Registro de Legionella realizado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getLegionellaLogs(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("registerFeeding():"+JSON.stringify(error));
        this.toastService.show("No se ha podido realizar el registro de alimentación.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }


  deleteLegionellaLog(id : string)
  {
    this.legionelaLogsService.deleteLegionellaLog(id).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Registro de Legionella eliminado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getLegionellaLogs(this.page);
      },
      error => {
        this.isProcessing = false;
        console.error("deleteLegionellaLog():"+JSON.stringify(error));
        this.toastService.show("No se ha podido eliminar el registro de alimentación.",
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
