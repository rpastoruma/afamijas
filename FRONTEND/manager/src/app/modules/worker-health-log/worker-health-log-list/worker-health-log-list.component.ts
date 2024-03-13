import { Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import { PatientDTO, parseDataExport, ActionDTO, HealthLogDTO } from 'src/app/shared/models/models';
import { UsersService } from 'src/app/core/services/users.service';
import { PdfService } from 'src/app/core/services/pdf-service.service';
import { ExcelService } from 'src/app/core/services/excel-service.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { flatpickrFactory } from '../../calendar/mycalendar.module'; 
import { HealthlogsService } from 'src/app/core/services/healthlogs.service';



@Component({
  selector: 'app-worker-health-log-list',
  templateUrl: './worker-health-log-list.component.html',
  styleUrls: ['./worker-health-log-list.component.scss']
})
export class WorkerHealthLogListComponent implements OnInit {

  

  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;

  //PARÁMETROS LISTADO
  dayfrom : Date = new Date();
  dayto : Date = new Date();
  groupcode : string = 'GRUPO 1';
  idpatient : string = '';

  page : number = 0;
  size : number = 4;
  totalPages : number = 0;

  allPatients : PatientDTO[] = [];
  allGroups : string[] = ['GRUPO 1', 'GRUPO 2', 'GRUPO 3']

  theHealthLog : HealthLogDTO = {
    id: '',
    idpatient: '',
    patient_fullname: '',
    idworker: '',
    worker_fullname: '',
    day: new Date(),
    low_pressure: 0,
    high_pressure: 0,
    hour_presure: '',
    sugar: 0,
    hour_sugar: ''
  };
  
  healthlogs: any[]  = []; // amy => formato del listado
  healthlogsObjects: HealthLogDTO[]  = [];

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
    private healthLogsService : HealthlogsService

  ) { }

  ngOnInit(): void {
    flatpickrFactory();
    this.getPatients();
    if(this.canModify()) this.actions = [{action: 'edit', text: 'Modificar registro sanitario'},{action: 'delete', text: 'Eliminar registro sanitario'}];
  }

  getPatients()
  {
      this.healthlogs = [];
      this.healthlogsObjects = [];
      this.isProcessing = true;
      this.usersService.getAllPatients(this.groupcode).subscribe(
        res => {
          this.allPatients = res;
          if(this.allPatients && this.allPatients.length>0) 
          {
            this.getHealthLogs(0);
          }
          else
            this.isProcessing = false;
        },
        error => 
        {
          console.error("getPatients():"+JSON.stringify(error));
          this.toastService.show("No se pueden obtener los usuarios.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
        }
      );
  }
  


  getHealthLogs(page :number) 
  {
    this.isProcessing = true;
    if(!this.dayfrom) this.dayfrom = new Date();
    if(!this.dayto) this.dayto = new Date();
    this.page = page
    this.healthLogsService.getHealthLogs(this.page, this.size, this.groupcode, this.idpatient, this.dayfrom, this.dayto).subscribe(
      res => {
        this.isProcessing = false;
        this.healthlogs = res.content.map(item => { return {id: item.id, values: [item.patient_fullname, this.date2Text1(item.day), item.high_pressure, item.low_pressure, item.hour_presure, item.sugar, item.hour_sugar, item.worker_fullname] }; });
        this.healthlogsObjects = res.content;

        this.totalPages = res.totalPages;
      },
      error => {
        this.isProcessing = false;
        console.error("getHealthLogs():"+JSON.stringify(error));
        this.toastService.show("No se ha podido obtener la información del registro sanitario.",
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

    this.healthLogsService.getHealthLogs(0, 100000000, this.groupcode, this.idpatient, this.dayfrom, this.dayto).subscribe(
      res => {
        const header = {};
        const keys = ['Usuario:', 'Día:', 'Tensión sistólica (alta):', 'Tensión diastólica (baja):', 'Hora tensión:', 'Nivel de azúcar (mg/dl):', 'Hora azúcar:', 'Registrado por:'];
        const fields = ['patient_fullname', 'day', 'high_pressure', 'low_pressure', 'hour_pressure', 'sugar', 'hour_sugar', 'worker_fullname'];
        fields.forEach((key, i) => header[key] = keys[i]);
        this.exportData = res && res.content ? res.content.map(item => [item.patient_fullname, this.date2Text1(item.day), item.daymeal, item.dish, item.result, item.indications, item.incidences, item.worker_fullname]) : null;
        const final = parseDataExport(fields, this.exportData);
        const title = 'Registro sanitario';

        if (format === 'excel') 
        {
          this.excelService.exportAsExcelFile(title, [header].concat(final), title, fields);
          this.loadingExcel = false;
          this.loadingPDF = false;
        } 
        else 
        {
          const minFields = ['20%','10%','10%','10%','10%','10%','10%','20%'];
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
    this.getHealthLogs(page);
  }


  setPage(event) {
    this.page = event;
    this.getHealthLogs(this.page);
  }

  action(event) 
  {
    if (event && event[0] === 'edit') 
    {
      const selected = this.healthlogsObjects.find(item => item.id === event[1]);
      this.theHealthLog = selected;

      this.openHealthLogModal(false);
    } 
    else if (event && event[0] === 'delete') 
    {
      this.deleteHealthLog(event[1]);
    }
  }


  canModify() : boolean
  {
    return this.authService.isNursingAssitant() || this.authService.isNursing();
  }



  openHealthLogModal(isNew : boolean)
  {
    if(this.theHealthLog.idpatient=='' && this.allPatients.length>0)  this.theHealthLog.idpatient = this.allPatients[0].id; 

    if(isNew)
    {
      this.theHealthLog.id = '';
      this.theHealthLog.low_pressure = 0;
      this.theHealthLog.high_pressure = 0;
      this.theHealthLog.sugar = 0;
    }

    this.modal.open(this.modalContent, { size: 'lg' });
  }

  registerHealthLog()
  {
    if(!this.theHealthLog.idpatient)
    {
      this.toastService.show("Debes seleccionar usuario.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }

    if(this.theHealthLog.low_pressure == 0 && this.theHealthLog.high_pressure == 0 && this.theHealthLog.sugar == 0)
    {
      this.toastService.show("Debes indicar la presión arterial y/o la medición del azúcar en sangre.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }


    this.healthLogsService.registerHealthLog(this.theHealthLog).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Registro sanitario realizado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getHealthLogs(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("registerHealthLog():"+JSON.stringify(error));
        this.toastService.show("No se ha podido realizar el registro sanitario.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }


  deleteHealthLog(id : string)
  {
    this.healthLogsService.deleteHealthLog(id).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Registro sanitario eliminado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getHealthLogs(this.page);
      },
      error => {
        this.isProcessing = false;
        console.error("deleteHealthLog():"+JSON.stringify(error));
        this.toastService.show("No se ha podido eliminar el registro sanitario.",
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
