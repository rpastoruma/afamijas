import { Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import { parseDataExport, ActionDTO,  TempFridgeDTO, TempServiceDTO, MealSampleDTO } from 'src/app/shared/models/models';
import { PdfService } from 'src/app/core/services/pdf-service.service';
import { ExcelService } from 'src/app/core/services/excel-service.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TempsService } from 'src/app/core/services/temps.service';
import { flatpickrFactory } from '../../calendar/mycalendar.module'; 



@Component({
  selector: 'app-worker-temp-register-list',
  templateUrl: './worker-temp-register-list.component.html',
  styleUrls: ['./worker-temp-register-list.component.scss']
})
export class WorkerTempRegisterListComponent implements OnInit{

  

  @ViewChild('modalContentA', { static: true }) modalContentA: TemplateRef<any>;
  @ViewChild('modalContentB', { static: true }) modalContentB: TemplateRef<any>;
  @ViewChild('modalContentC', { static: true }) modalContentC: TemplateRef<any>;

  //PARÁMETROS LISTADO
  dayfrom : Date = new Date();
  dayto  : Date = new Date();

  page : number = 0;
  size : number = 5;
  totalPages : number = 0;

  theTempA : TempFridgeDTO = {
    id: '',
    idworker: '',
    worker_fullname: '',
    day: new Date(),
    temperature: 4.0,
    isOk: false
  };
  
  theTempB : TempServiceDTO = {
    id: '',
    idworker: '',
    worker_fullname: '',
    day: new Date(),
    dish: '',
    dish_type: 'FRÍO',
    temperature_reception: 4.0,
    temperature_service: undefined,
    isOk: false
  };
  
  theMealSample : MealSampleDTO = {
    id: '',
    idworker: '',
    worker_fullname: '',
    day: new Date(),
    dish: '',
    orgenolepticoOk: true,
    cuerposExtraOk: true,
    comments: ''
  };
  
  tempsA: any[]  = []; // amy => formato del listado
  tempsB: any[]  = []; // amy => formato del listado
  mealsSamples: any[]  = []; // amy => formato del listado

  tempsAObjects: TempFridgeDTO[]  = [];
  tempsBObjects: TempServiceDTO[]  = [];
  mealsSamplesObjects: MealSampleDTO[]  = [];

  loadingExcel : boolean = false;
  loadingPDF : boolean = false;
  isProcessing : boolean = true;

  exportData: any[];

  actions : ActionDTO[] = [];

  constructor(
    public toastService: NbToastrService,
    private pdfService : PdfService,
    private excelService : ExcelService,
    private authService : AuthService,
    private modal: NgbModal, 
    private tempsService : TempsService

  ) { }

  ngOnInit(): void {
    flatpickrFactory();
    if(this.canModify()) this.actions = [{action: 'edit', text: 'Modificar registro'},{action: 'delete', text: 'Eliminar registro'}];
    this.getTempFridges(0);
    this.getTempServices(0);
    this.getMealSamples(0)
  }




  getTempFridges(page :number) 
  {
    if(!this.dayfrom) this.dayfrom = new Date();
    if(!this.dayto) this.dayto = this.dayfrom;

    this.dayfrom.setHours(0,0,0,0);
    this.dayto.setHours(0,0,0,0);

    if(this.dayto < this.dayfrom)  
    { 
      this.toastService.show("Fecha de inicio posterior a fecha de fin.",
      "¡Ups!", 
      { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return; 
    } 

    this.tempsA = [];
    this.tempsAObjects = [];
    this.isProcessing = true;
    
    this.page = page
    this.tempsService.getTempFridges(this.page, this.size, this.dayfrom, this.dayto).subscribe(
      res => {
        this.isProcessing = false;
        this.tempsA = res.content.map(item => { return {id: item.id, values: [this.date2Text1(item.day), this.getTemp(item.temperature, item.ok), item.worker_fullname] }; });
        this.tempsAObjects = res.content;

        this.totalPages = res.totalPages;
      },
      error => {
        this.isProcessing = false;
        console.error("getTempFridges():"+JSON.stringify(error));
        this.toastService.show("No se ha podido obtener la información del registro de temperatura.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }



  

  getTempServices(page :number) 
  {
    if(!this.dayfrom) this.dayfrom = new Date();
    if(!this.dayto) this.dayto = this.dayfrom;

    this.dayfrom.setHours(0,0,0,0);
    this.dayto.setHours(0,0,0,0);

    if(this.dayto < this.dayfrom)  
    { 
      this.toastService.show("Fecha de inicio posterior a fecha de fin.",
      "¡Ups!", 
      { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return; 
    } 

    this.tempsB = [];
    this.tempsBObjects = [];
    this.isProcessing = true;
    
    this.page = page
    this.tempsService.getTempServices(this.page, this.size, this.dayfrom, this.dayto).subscribe(
      res => {
        this.isProcessing = false;
        this.tempsB = res.content.map(item => { return {id: item.id, values: [this.date2Text1(item.day), item.dish, item.dish_type, this.getTempReception(item.temperature_reception), this.getTempService(item.temperature_service, item.dish_type), item.worker_fullname] }; });
        this.tempsBObjects = res.content;

        this.totalPages = res.totalPages;
      },
      error => {
        this.isProcessing = false;
        console.error("getTempServices():"+JSON.stringify(error));
        this.toastService.show("No se ha podido obtener la información del registro de temperatura.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }


  

  getMealSamples(page :number) 
  {
    if(!this.dayfrom) this.dayfrom = new Date();
    if(!this.dayto) this.dayto = this.dayfrom;

    this.dayfrom.setHours(0,0,0,0);
    this.dayto.setHours(0,0,0,0);

    if(this.dayto < this.dayfrom)  
    { 
      this.toastService.show("Fecha de inicio posterior a fecha de fin.",
      "¡Ups!", 
      { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return; 
    } 

    this.mealsSamples = [];
    this.mealsSamplesObjects = [];
    this.isProcessing = true;
    
    this.page = page
    this.tempsService.getMealSamples(this.page, this.size, this.dayfrom, this.dayto).subscribe(
      res => {
        this.isProcessing = false;
        this.mealsSamples = res.content.map(item => { return {id: item.id, values: [this.date2Text1(item.day), item.dish, this.getBoolean(item.orgenolepticoOk, false), this.getBoolean(item.cuerposExtraOk, false), item.comments, item.worker_fullname] }; });
        this.mealsSamplesObjects = res.content;

        this.totalPages = res.totalPages;
      },
      error => {
        this.isProcessing = false;
        console.error("getMealSamples():"+JSON.stringify(error));
        this.toastService.show("No se ha podido obtener la información del registro de temperatura.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }

  getTemp(temp : number, ok :boolean)
  {
    const stemp = temp>0?'+'+temp:temp;
    if(ok) return "<div class=\"green\">" + stemp + " ºC</div>";
    else return "<div class=\"red\">" + stemp + " ºC</div>";
  }

  
  getTempReception(temp : number)
  {
    if(!temp) return '';
    const stemp = temp>0?'+'+temp:temp;
    if(temp<=4) return "<div class=\"green\">" + stemp + " ºC</div>";
    else return "<div class=\"red\">" + stemp + " ºC</div>";
  }

  
  getTempService(temp : number, dish_type : string)
  {
    if(!temp) return '';
    const stemp = temp>0?'+'+temp:temp;

    if(dish_type == 'FRÍO')
    {
      if(temp<=8) return "<div class=\"green\">" + stemp + " ºC</div>";
      else return "<div class=\"red\">" + stemp + " ºC</div>";
    }
    else if(dish_type == 'CALIENTE')
    {
      if(temp>=65) return "<div class=\"green\">" + stemp + " ºC</div>";
      else return "<div class=\"red\">" + stemp + " ºC</div>";
    }
    else return stemp; //NO DEBERÍA DARSE

  }


  getBoolean(x : boolean, pdf :boolean)
  {
    if(!pdf)
    {
      if(x) return "<span class='green'>BIEN</span>"; else return "<span class='red'>MAL</span>";
    }
    else
    {
      if(x) return "BIEN"; else return "MAL";
    }
  }

  
  getExportDataA(format: string) 
  {
    if(format === 'excel') this.loadingExcel = true;
    else if(format === 'pdf') this.loadingPDF = true;


    this.tempsService.getTempFridges(0, 100000000, this.dayfrom, this.dayto).subscribe(
      res => {
        const header = {};
        const keys = ['Día:', 'Temperatura:', 'Registrado por:'];
        const fields = ['day', 'temperature', 'worker_fullname'];
        fields.forEach((key, i) => header[key] = keys[i]);
        this.exportData = res && res.content ? res.content.map(item => [this.date2Text1(item.day), item.temperature, item.worker_fullname]) : null;
        const final = parseDataExport(fields, this.exportData);
        const title = 'Registro de temperatura de nevera';

        if (format === 'excel') 
        {
          this.excelService.exportAsExcelFile(title, [header].concat(final), title, fields);
          this.loadingExcel = false;
          this.loadingPDF = false;
        } 
        else 
        {
          const minFields = ['20%','10%','70%'];
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



  getExportDataB(format: string) 
  {
    if(format === 'excel') this.loadingExcel = true;
    else if(format === 'pdf') this.loadingPDF = true;


    this.tempsService.getTempServices(0, 100000000, this.dayfrom, this.dayto).subscribe(
      res => {
        const header = {};
        const keys = ['Día:', 'Plato:', 'Tipo:', 'Temperatura de repeción:', 'Temperatura de servicio:', 'Registrado por:'];
        const fields = ['day', 'dish', 'dish_type', 'temperature_reception', 'temperature_service', 'worker_fullname'];
        fields.forEach((key, i) => header[key] = keys[i]);
        this.exportData = res && res.content ? res.content.map(item => [this.date2Text1(item.day), item.temperature, item.worker_fullname]) : null;
        const final = parseDataExport(fields, this.exportData);
        const title = 'Registro de temperatura de recepción y servicio';

        if (format === 'excel') 
        {
          this.excelService.exportAsExcelFile(title, [header].concat(final), title, fields);
          this.loadingExcel = false;
          this.loadingPDF = false;
        } 
        else 
        {
          const minFields = ['20%','20%','20%','10%','10%','20%'];
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


  


  getExportDataC(format: string) 
  {
    if(format === 'excel') this.loadingExcel = true;
    else if(format === 'pdf') this.loadingPDF = true;


    this.tempsService.getMealSamples(0, 100000000, this.dayfrom, this.dayto).subscribe(
      res => {
        const header = {};
        const keys = ['Día:', 'Plato:', 'Control organoléptico:', 'Control cuerpos extraños:', 'Observaciones:', 'Registrado por:'];
        const fields = ['day', 'dish', 'orgenolepticoOk', 'cuerposExtraOk', 'comments', 'worker_fullname'];
        fields.forEach((key, i) => header[key] = keys[i]);
        this.exportData = res && res.content ? res.content.map(item => [this.date2Text1(item.day), item.dish, this.getBoolean(item.orgenolepticoOk, true), this.getBoolean(item.cuerposExtraOk, true), item.comments, item.worker_fullname]) : null;
        const final = parseDataExport(fields, this.exportData);
        const title = 'Registro de muestras de comidas';

        if (format === 'excel') 
        {
          this.excelService.exportAsExcelFile(title, [header].concat(final), title, fields);
          this.loadingExcel = false;
          this.loadingPDF = false;
        } 
        else 
        {
          const minFields = ['10%','20%','10%','10%','30%','20%'];
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


  filterA(page: number) {
    this.getTempFridges(page);
  }


  filterB(page: number) {
    this.getTempServices(page);
  }


  filterC(page: number) {
    this.getMealSamples(page);
  }


  setPageA(event) {
    this.page = event;
    this.getTempFridges(this.page);
  }

  setPageB(event) {
    this.page = event;
    this.getTempServices(this.page);
  }

  setPageC(event) {
    this.page = event;
    this.getMealSamples(this.page);
  }

  actionA(event) 
  {
    if (event && event[0] === 'edit') 
    {
      const selected = this.tempsAObjects.find(item => item.id === event[1]);
      this.theTempA = selected;

      console.log("selected=" + JSON.stringify(selected));

      this.openRegisterTempFridgeModal(false);
    } 
    else if (event && event[0] === 'delete') 
    {
      this.deleteTempFridge(event[1]);
    }
  }


  actionB(event) 
  {
    if (event && event[0] === 'edit') 
    {
      const selected = this.tempsBObjects.find(item => item.id === event[1]);
      this.theTempB = selected;

      console.log("selected=" + JSON.stringify(selected));

      this.openRegisterTempServiceModal(false);
    } 
    else if (event && event[0] === 'delete') 
    {
      this.deleteTempService(event[1]);
    }
  }


  

  actionC(event) 
  {
    if (event && event[0] === 'edit') 
    {
      const selected = this.mealsSamplesObjects.find(item => item.id === event[1]);
      this.theMealSample = selected;

      console.log("selected=" + JSON.stringify(selected));

      this.openRegisterMealSampleModal(false);
    } 
    else if (event && event[0] === 'delete') 
    {
      this.deleteMealSample(event[1]);
    }
  }


  canModify() : boolean
  {
    return this.authService.isKitchen();
  }



  openRegisterTempFridgeModal(isNew : boolean)
  {
    if(isNew) this.theTempA.id = '';
    this.modal.open(this.modalContentA, { size: 'lg' });
  }

  openRegisterTempServiceModal(isNew : boolean)
  {
    if(isNew) { this.theTempB.id = ''; this.theTempB.dish = ''; this.theTempB.temperature_service = undefined;}
    this.modal.open(this.modalContentB, { size: 'lg' });
  }

  openRegisterMealSampleModal(isNew : boolean)
  {
    if(isNew) { this.theMealSample.id = ''; this.theMealSample.dish = ''; }
    this.modal.open(this.modalContentC, { size: 'lg' });
  }

  
  registerTempFridge()
  {
    if(!this.theTempA.day) this.theTempA.day = new Date();

    if(!this.theTempA.temperature)
    {
      this.toastService.show("Debes introducir una temperatura.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }

    this.tempsService.registerTempFridge(this.theTempA).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Registro de temperatura realizado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getTempFridges(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("registerFeeding():"+JSON.stringify(error));
        this.toastService.show("No se ha podido realizar el registro de temperatura.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }





  registerTempService()
  {
    if(!this.theTempB.day) this.theTempB.day = new Date();


    if(this.theTempB.dish == '')
    {
      this.toastService.show("Debes introducir el nombre del plato.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }

    if(!this.theTempB.temperature_reception && !this.theTempB.temperature_service)
    {
      this.toastService.show("Debes introducir una temperatura de recepción y/o servicio.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }

    this.tempsService.registerTempService(this.theTempB).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Registro de temperatura realizado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getTempServices(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("registerTempService():"+JSON.stringify(error));
        this.toastService.show("No se ha podido realizar el registro de temperatura.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }


  



  registerMealSample()
  {
    if(!this.theMealSample.day) this.theMealSample.day = new Date();


    if(this.theMealSample.dish == '')
    {
      this.toastService.show("Debes introducir el nombre del plato.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }


    this.tempsService.registerMealSample(this.theMealSample).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Registro de muestra de comida realizado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getMealSamples(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("registerMealSample():"+JSON.stringify(error));
        this.toastService.show("No se ha podido realizar el registro de muestra de comida.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }



  deleteTempFridge(id : string)
  {
    this.tempsService.deleteTempFridge(id).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Registro de temperatura eliminado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getTempFridges(this.page);
      },
      error => {
        this.isProcessing = false;
        console.error("deleteTempFridge():"+JSON.stringify(error));
        this.toastService.show("No se ha podido eliminar el registro de temperatura.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }



  deleteTempService(id : string)
  {
    this.tempsService.deleteTempService(id).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Registro de temperatura eliminado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getTempServices(this.page);
      },
      error => {
        this.isProcessing = false;
        console.error("deleteTempService():"+JSON.stringify(error));
        this.toastService.show("No se ha podido eliminar el registro de temperatura.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }


  deleteMealSample(id : string)
  {
    this.tempsService.deleteMealSample(id).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Registro de muestra de comida eliminado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getMealSamples(this.page);
      },
      error => {
        this.isProcessing = false;
        console.error("deleteMealSample():"+JSON.stringify(error));
        this.toastService.show("No se ha podido eliminar el registro de muestra de comida.",
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
