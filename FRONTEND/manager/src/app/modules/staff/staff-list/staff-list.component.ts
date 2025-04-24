import { Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { NbDialogService, NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import {  parseDataExport, ActionDTO, CountryDTO, StateDTO, CityDTO, WorkerDTO } from 'src/app/shared/models/models';
import { UsersService } from 'src/app/core/services/users.service';
import { PdfService } from 'src/app/core/services/pdf-service.service';
import { ExcelService } from 'src/app/core/services/excel-service.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FrontValuesService } from 'src/app/core/services/front-values.service';
import { isPhone } from 'spain-phone';
import * as EmailValidator from 'email-validator';
import { CalendarEvent } from 'angular-calendar';

@Component({
  selector: 'app-staff-list',
  templateUrl: './staff-list.component.html',
  styleUrls: ['./staff-list.component.scss']
})
export class StaffListComponent  implements OnInit{

  
  workerRoles: string[] = [
    'ADMIN',
    'NURSING_ASSISTANT',
    'KITCHEN',
    'LEGIONELLA_CONTROL',
    'MANAGER',
    'NURSING',
    'PHYSIOTHERAPIST',
    'CLEANING',
    'MONITOR',
    'OCCUPATIONAL_THERAPIST',
    'OPERATOR_EXTRA_1',
    'PSYCHOLOGIST',
    'SOCIAL_WORKER',
    'TRANSPORT'
  ];
  
  
  workerRoleLabels: { [key: string]: string } = {
    TRANSPORT: 'Transporte',
    ADMIN: 'Administración',
    CLEANING: 'Limpieza',
    NURSING: 'Enfermería',
    NURSING_ASSISTANT: 'Auxiliar de enfermería',
    LEGIONELLA_CONTROL: 'Control de legionela',
    KITCHEN: 'Cocina',
    MONITOR: 'Monitor/a',
    SOCIAL_WORKER: 'Trabajador/a social',
    PSYCHOLOGIST: 'Psicólogo/a',
    MANAGER: 'Dirección',
    PHYSIOTHERAPIST: 'Fisioterapeuta',
    OCCUPATIONAL_THERAPIST: 'Terapeuta ocupacional',
    OPERATOR_EXTRA_1: 'Operador extra'
  };
  
  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;

  @ViewChild('modalUnregister', { static: true }) modalUnregister: TemplateRef<any>;

  @ViewChild('modalEvents', { static: true }) modalEvents: TemplateRef<any>;

  page : number = 0;
  size : number = 5;
  totalPages : number = 0;

  role : string = undefined;
  name_surnames : string = '';
  documentid : string;
  status : string = 'A';

  workers: any[]  = []; // amy => formato del listado
  workersObjects: WorkerDTO[]  = [];

  loadingExcel : boolean = false;
  loadingPDF : boolean = false;
  isProcessing : boolean = true;

  exportData: any[];

  actions : ActionDTO[] = [];

  theWorker : WorkerDTO = {
    id: '',
    roles: [],
    email: '',
    name: '',
    surname1: '',
    surname2: '',
    fullname: '',
    documentid: '',
    documenttype: 'DNI',
    phone: '',
    postaladdress: '',
    idcity: undefined,
    cityname: '',
    idstate: undefined,
    statename: '',
    idcountry: undefined,
    countryname: '',
    postalcode: '',
    status: 'A',
    password: '',
    nss: '',
    categoria_profesional: '',
    tipo_contrato: '',
    jornada_laboral: '',
    horario: ''
  }

  theCountries : CountryDTO[] = [];
  theStates : StateDTO[] = [];
  theCities : CityDTO[] = [];



  theNewWorker : WorkerDTO = { ...this.theWorker };

  constructor(
    public toastService: NbToastrService,
    private usersService : UsersService,
    private pdfService : PdfService,
    private excelService : ExcelService,
    private modal: NgbModal, 
    private frontValuesService : FrontValuesService

  )
   {
    }

  ngOnInit(): void {
    this.getWorkers(0);
    this.actions = [  {action: 'show', text: 'Eventos del trabajador'},  {action: 'book', text: 'Nóminas del trabajador'},  {action: 'edit', text: 'Modificar datos del trabajador'},   {action: 'delete', text: 'Eliminar al trabajador'}  ];
  }

  getWorkers(page? : number)
  {
      this.isProcessing = true;
      if(page) this.page = page;
      this.usersService.getWorkers(this.page, this.size, this.role, this.name_surnames, this.documentid, this.status).subscribe(
        res => {
          this.isProcessing = false;

          //this.workers = res.content.map(item => { return {id: item.id, values: [item.roles.toString(), item.fullname, item.documentid, item.email, item.phone]  }; });

          this.workers = res.content.map(item => {

            item.events = item.events.map(x => this.wrapCalendarEvent(x));

            // Convertir los roles a sus valores legibles y unirlos con coma
            const rolesLegibles = item.roles
              .map(role => this.workerRoleLabels[role]) // Convierte cada rol al texto legible
              .join(', '); // Une los roles con coma
          
            return {
              id: item.id,
              values: [rolesLegibles, item.fullname, item.documentid, item.email, item.phone]
            };

            
          });


          this.workersObjects = res.content;
          this.totalPages = res.totalPages;
        },
        error => 
        {
          this.isProcessing = false;
          console.error("getworkers():"+JSON.stringify(error));
          this.toastService.show("No se pueden obtener los trabajadores.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
        }
      );
  }
  


  getCountries() 
  {
    this.isProcessing = true;
    this.frontValuesService.getCountries().subscribe(
      res => {
        this.isProcessing = false;
        this.theCountries = res;
        if(!this.theWorker.idcountry) this.theWorker.idcountry = 207; //ESPAÑA
        this.getStates();
      },
      error => {
        this.isProcessing = false;
        console.error("getCountries():"+JSON.stringify(error));
        this.toastService.show("No se han podido obtener los países.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }
  
  

  getStates() 
  {
    this.isProcessing = true;
    this.frontValuesService.getStates(this.theWorker.idcountry).subscribe(
      res => {
        this.isProcessing = false;
        this.theStates = res;
        if(this.theStates[0]) { if(!this.theWorker.idstate) this.theWorker.idstate = this.theStates[0].id; }
        else this.theWorker.idstate = undefined;
        this.getCities();
      },
      error => {
        this.isProcessing = false;
        console.error("getStates():"+JSON.stringify(error));
        this.toastService.show("No se han podido obtener los estados.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }
  

  getCities() 
  {
    this.isProcessing = true;
    this.frontValuesService.getCities(this.theWorker.idstate).subscribe(
      res => {
        this.isProcessing = false;
        this.theCities = res;
        if(this.theCities[0]) { if(!this.theWorker.idcity) this.theWorker.idcity = this.theCities[0].id; }
        else this.theWorker.idcity = undefined;
      },
      error => {
        this.isProcessing = false;
        console.error("getStates():"+JSON.stringify(error));
        this.toastService.show("No se han podido obtener las ciudades.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }
  
  

  getStateAndCitiesByPostalCodeAndCountryId() 
  {
    if(this.theWorker.idcountry!=207) return; //solo para españa
    this.isProcessing = true;
    this.frontValuesService.getStateAndCitiesByPostalCodeAndCountryId(this.theWorker.postalcode, this.theWorker.idcountry).subscribe(
      res => {
        this.isProcessing = false;
        if(res)
        {
          this.theWorker.idstate = res.state.id;
          this.theCities = res.cities;
          if(this.theCities[0]) this.theWorker.idcity = this.theCities[0].id;
          else this.theWorker.idcity = undefined;
        }
        else
        {
          this.theWorker.idstate = undefined;
          this.theWorker.idcity = undefined;
        }
      },
      error => {
        this.isProcessing = false;
        console.error("getStateAndCitiesByPostalCodeAndCountryId():"+JSON.stringify(error));
        /*
        this.toastService.show("No se han podido obtener las ciudades por código postal.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );*/
      }
    );
  }
  
  
  
  getExportData(format: string) 
  {
    if(format === 'excel') this.loadingExcel = true;
    else if(format === 'pdf') this.loadingPDF = true;

    this.usersService.getWorkers(0, 100000000, this.role, this.name_surnames, this.documentid, this.status).subscribe(
      res => {
        const header = {};
        const keys = ['Perfil', 'Nombre', 'DNI', 'Email', 'Teléfono'];
        const fields = ['roles', 'fullname', 'documentid', 'email', 'phone'];
        fields.forEach((key, i) => header[key] = keys[i]);
        //this.exportData = res && res.content ? res.content.map(item => [item.roles.toString(), item.fullname, item.documentid, item.email, item.phone]) : null;

        this.exportData = res && res.content 
  ? res.content.map(item => [
      item.roles
        .map(role => this.workerRoleLabels[role]) // Convierte cada rol al texto legible
        .join(', '), // Une los roles con coma
      item.fullname,
      item.documentid,
      item.email,
      item.phone
    ]) 
  : null;


        const final = parseDataExport(fields, this.exportData);
        const title = 'Listado de trabajadores';

        if (format === 'excel') 
        {
          this.excelService.exportAsExcelFile(title, [header].concat(final), title, fields);
          this.loadingExcel = false;
          this.loadingPDF = false;
        } 
        else 
        {
          const minFields = ['20%','30%','10%','20%','10%'];
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
    this.getWorkers(page);
  }


  setPage(event) {
    this.page = event;
    this.getWorkers(this.page);
  }

  action(event) 
  {
    if (event && event[0] === 'show') 
    {
      const selected = this.workersObjects.find(item => item.id === event[1]);
      this.theWorker = selected;

      this.openShowEventsModal(false);
    } 
    else if (event && event[0] === 'book') 
    {
      const selected = this.workersObjects.find(item => item.id === event[1]);
      this.theWorker = selected;

      window.open('/staff/nominas?idworker=' + this.theWorker.id);
    } 
    else if (event && event[0] === 'edit') 
    {
      const selected = this.workersObjects.find(item => item.id === event[1]);
      this.theWorker = selected;

      this.openSaveWorkerModal(false);
    } 
    else if (event && event[0] === 'delete') 
    {
      const selected = this.workersObjects.find(item => item.id === event[1]);
      this.theWorker = selected;

      this.openUnregistereWorkerModal();
    }
  }



  openSaveWorkerModal(isnew : boolean)
  {
    if(isnew) this.theWorker = {...this.theNewWorker};

    this.getCountries();


    this.modal.open(this.modalContent, { size: 'lg' });
  }

  
  openUnregistereWorkerModal()
  {

    this.modal.open(this.modalUnregister, { size: 'lg' });
  }



  openShowEventsModal(isnew : boolean)
  {

    this.modal.open(this.modalEvents, { size: 'lg' });
  }


  
  saveWorker()
  {
    
    this.usersService.saveWorker(this.theWorker).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Datos del trabajador grabados correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getWorkers(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("saveworker():"+JSON.stringify(error));
        this.toastService.show("No se ha podido grabar el trabajador correctamente.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    ); 
  }





  

  unregisterWorker()
  {
    
    this.usersService.unregisterWorker(this.theWorker.id).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Trabajador dado de baja correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
          this.page = 0;
        this.getWorkers(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("modifyFood():"+JSON.stringify(error));
        this.toastService.show("No se ha podido dar de baja el trabajador correctamente.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    ); 
  }


  goStep(step :string)
  {
    document.getElementById('step1').style.display = 'none';
    document.getElementById('step2').style.display = 'none';
    document.getElementById('step3').style.display = 'none';

    document.getElementById(step).style.display = 'block';
  }

  getFullName()
  {
    return (this.theWorker.name.trim() + " " + this.theWorker.surname1.trim() + " " + this.theWorker.surname2.trim()).trim();
  }






  validEmail(email :string)
  {
    return EmailValidator.validate(email);
  }
  


  validDocumentId(documentId: string, documentType: string)
  {
    if(documentId == null || documentId.length == 0) return false;
    if(documentType == null || documentType.length == 0) return false;
 
    documentId = documentId.toUpperCase();
    this.theWorker.documentid = documentId;

    const dni_letters = "TRWAGMYFPDXBNJZSQVHLCKE";
    if(documentType == 'DNI')
    {
      let letter = dni_letters.charAt( parseInt( documentId, 10 ) % 23 );
      if(letter == documentId.charAt(8) && documentId.length==9) return true;
      else return false;
    }
    else if(documentType == 'NIE')
    {
      if(this.validDocumentId(documentId, 'DNI')) return false;

      let nie_prefix = documentId.charAt( 0 );
      switch (nie_prefix)
      {
          case 'X': nie_prefix = '0'; break;
          case 'Y': nie_prefix = '1'; break;
          case 'Z': nie_prefix = '2'; break;
      }
      const dni = nie_prefix + documentId.substring(1);
      let letter = dni_letters.charAt( parseInt( dni, 10 ) % 23 );
  
      if(letter == dni.charAt(8)  && documentId.length==9) return true;
      else return false;
    }
    else
      return true;
  }

  arrayToDate(dateArray: number[]): Date {
    if (!dateArray || dateArray.length < 3) return null;
    const [year, month, day, hour = 0, minute = 0] = dateArray;
    return new Date(year, month - 1, day, hour, minute);
  }



  wrapCalendarEvent(x)
  {
    if(!x) return null;

    console.log("x.start="+x.start);
    console.log("x.end="+x.end);
    console.log("x.publishdate="+x.publishdate);


    let wrapped : CalendarEvent = x;

    wrapped.start = this.arrayToDate(x.start);
    if(x.end) wrapped.end = this.arrayToDate(x.end);
    if(x.publishdate) wrapped.publishdate = this.arrayToDate(x.publishdate);

    console.log("wrapped.start="+wrapped.start);
    console.log("wrapped.end="+wrapped.end);
    console.log("wrapped.publishdate="+wrapped.publishdate);

    return wrapped;
  }


  


}
