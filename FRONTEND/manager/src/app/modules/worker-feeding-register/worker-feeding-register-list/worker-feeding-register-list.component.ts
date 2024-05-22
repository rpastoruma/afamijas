import { Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import { PatientDTO, parseDataExport, ActionDTO, FeedingDTO } from 'src/app/shared/models/models';
import { UsersService } from 'src/app/core/services/users.service';
import { PdfService } from 'src/app/core/services/pdf-service.service';
import { ExcelService } from 'src/app/core/services/excel-service.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FeedingsService } from 'src/app/core/services/feedings.service';
import { flatpickrFactory } from '../../calendar/mycalendar.module'; 



@Component({
  selector: 'app-worker-feeding-register-list',
  templateUrl: './worker-feeding-register-list.component.html',
  styleUrls: ['./worker-feeding-register-list.component.scss']
})
export class WorkerFeedingRegisterListComponent implements OnInit {

  

  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;

  //PARÁMETROS LISTADO
  dayfrom : Date = new Date();
  dayto : Date = new Date();
  groupcode : string = 'GRUPO 1';
  idpatient : string = '';
  idspatients : string[] = [];
  all_idspatients : string[] = [];

  page : number = 0;
  size : number = 4;
  totalPages : number = 0;

  allPatients : PatientDTO[] = [];
  allGroups : string[] = ['GRUPO 1', 'GRUPO 2', 'GRUPO 3']

  theFeeding : FeedingDTO = {
    id: '',
    idpatient: '',
    patient_fullname: '',
    idworker: '',
    worker_fullname: '',
    day: new Date(),
    daymeal: 'ALMUERZO',
    dish: '',
    result: '',
    indications: '',
    incidences: ''
  };
  
  feedings: any[]  = []; // amy => formato del listado
  feedingsObjects: FeedingDTO[]  = [];

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
    private feedingsService : FeedingsService

  ) { }

  ngOnInit(): void {
    flatpickrFactory();
    this.getPatients();
    if(this.canModify()) this.actions = [{action: 'edit', text: 'Modificar registro de alimentación'},{action: 'delete', text: 'Eliminar registro de alimentación'}];
  }

  getPatients()
  {
      this.feedings = [];
      this.feedingsObjects = [];
      this.isProcessing = true;
      this.usersService.getAllPatients(this.groupcode).subscribe(
        res => {
          this.allPatients = res;
          this.idspatients = this.allPatients.map(item => item.id);
          this.all_idspatients = this.allPatients.map(item => item.id);
          if(this.allPatients && this.allPatients.length>0) 
          {
            this.getFeedings(0);
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
  


  getFeedings(page :number) 
  {
    this.isProcessing = true;
    if(!this.dayfrom) this.dayfrom = new Date();
    if(!this.dayto) this.dayto = new Date();
    this.page = page
    this.feedingsService.getFeedings(this.page, this.size, this.groupcode, this.idpatient, this.dayfrom, this.dayto).subscribe(
      res => {
        this.isProcessing = false;
        this.feedings = res.content.map(item => { return {id: item.id, values: [item.patient_fullname, this.date2Text1(item.day), /*item.daymeal,*/ item.dish, item.result, item.indications, item.incidences, item.worker_fullname] }; });
        this.feedingsObjects = res.content;

        this.totalPages = res.totalPages;
      },
      error => {
        this.isProcessing = false;
        console.error("getFeedings():"+JSON.stringify(error));
        this.toastService.show("No se ha podido obtener la información del registro de alimentación.",
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

    this.feedingsService.getFeedings(0, 100000000, this.groupcode, this.idpatient, this.dayfrom, this.dayto).subscribe(
      res => {
        const header = {};
        const keys = ['Usuario:', 'Día:', 'Comida:', 'Plato:', 'Resultado:', 'Indicaciones:', 'Incidencias:', 'Registrado por:'];
        const fields = ['patient_fullname', 'day', 'daymeal', 'dish', 'result', 'indications', 'incidences', 'worker_fullname'];
        fields.forEach((key, i) => header[key] = keys[i]);
        this.exportData = res && res.content ? res.content.map(item => [item.patient_fullname, this.date2Text1(item.day), /*item.daymeal,*/ item.dish, item.result, item.indications, item.incidences, item.worker_fullname]) : null;
        const final = parseDataExport(fields, this.exportData);
        const title = 'Registro de alimentación';

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
    this.getFeedings(page);
  }


  setPage(event) {
    this.page = event;
    this.getFeedings(this.page);
  }

  action(event) 
  {
    if (event && event[0] === 'edit') 
    {
      const selected = this.feedingsObjects.find(item => item.id === event[1]);
      this.theFeeding = selected;

      this.openRegisterFeedinggModal();
    } 
    else if (event && event[0] === 'delete') 
    {
      this.deleteFeeding(event[1]);
    }
  }


  canModify() : boolean
  {
    return this.authService.isNursingAssitant();
  }



  openRegisterFeedinggModal()
  {
    this.idspatients = this.all_idspatients;
    //console.log("this.all_idspatients="+this.all_idspatients)
    if(this.theFeeding.idpatient=='' && this.allPatients.length>0)  this.theFeeding.idpatient = this.allPatients[0].id; 
    //if(this.theFeeding.daymeal=='' && new Date().getHours() <= 12) this.theFeeding.daymeal = 'DESAYUNO';
    //if(this.theFeeding.daymeal=='' && new Date().getHours() > 12) this.theFeeding.daymeal = 'ALMUERZO';
    this.theFeeding.daymeal = 'ALMUERZO';
    if(this.theFeeding.dish=='') this.theFeeding.dish = 'PRIMERO';
    if(this.theFeeding.result=='') this.theFeeding.result = 'COMPLETO';

    this.modal.open(this.modalContent, { size: 'lg' });
  }

  registerFeeding() 
  {
    if(!this.theFeeding.day) this.theFeeding.day = new Date();

    if(this.idspatients.length==0)
    {
      this.toastService.show("Debes seleccionar usuario.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }

    /*
    if(!this.theFeeding.daymeal)
    {
      this.toastService.show("Debes seleccionar comida.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }*/

    if(!this.theFeeding.dish && this.theFeeding.daymeal=='ALMUERZO')
    {
      this.toastService.show("Debes seleccionar plato.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }


    if(!this.theFeeding.result)
    {
      this.toastService.show("Debes seleccionar resultado.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }

    let isError = false;
    for(let i=0; i<this.idspatients.length; i++)
    {
      this.theFeeding.idpatient = this.idspatients[i];
      this.feedingsService.registerFeeding(this.theFeeding).subscribe(
        res => {
          this.isProcessing = false;
          if(i==this.idspatients.length-1)
          {
            if(!isError) this.toastService.show("Registro de alimentación realizado correctamente.",
                "¡Ok!", 
                { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
                );
            this.getFeedings(this.page);
            this.modal.dismissAll();
          }
        },
        error => {
          isError = true;
          this.isProcessing = false;
          console.error("registerFeeding():"+JSON.stringify(error));
          this.toastService.show("No se ha podido realizar el registro de alimentación.",
            "¡Ups!", 
            { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
           );
        }
      );
    }

  }


  deleteFeeding(id : string)
  {
    this.feedingsService.deleteFeeding(id).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Registro de alimentación eliminado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getFeedings(this.page);
      },
      error => {
        this.isProcessing = false;
        console.error("deleteFeeding():"+JSON.stringify(error));
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
