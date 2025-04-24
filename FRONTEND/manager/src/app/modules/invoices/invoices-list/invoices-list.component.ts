import { Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import { parseDataExport, ActionDTO, rolName, InvoiceDTO, MemberDTO } from 'src/app/shared/models/models';
import { UsersService } from 'src/app/core/services/users.service';
import { PdfService } from 'src/app/core/services/pdf-service.service';
import { ExcelService } from 'src/app/core/services/excel-service.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { flatpickrFactory } from '../../calendar/mycalendar.module'; 
import { Subscription, finalize } from 'rxjs';
import { MediaService } from 'src/app/core/services/media.service';
import { HttpEventType } from '@angular/common/http';
import { InvoicesService } from 'src/app/core/services/invoices.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-invoices-list',
  templateUrl: './invoices-list.component.html',
  styleUrls: ['./invoices-list.component.scss']
})
export class InvoicesListComponent implements OnInit {

  

  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;

  //PARÁMETROS LISTADO
  dayfrom : Date = null; 
  dayto : Date = null;
  idpatient : string = '';
  status : string = '';

  page : number = 0;
  size : number = 4;
  totalPages : number = 0;

  
  requiredFileType:string  = ".png,.jpg,.jpeg,.webp,.gif,.pdf,.doc,.docx,.xsl,.xslx";
  fileName = '';
  uploadProgress:number;
  uploadSub: Subscription;

  theInvoice : InvoiceDTO = {
    id: '',
    idpatient: '',
    patient_fullname: '',
    total: 0,
    url: '',
    duedate: undefined,
    paiddate: undefined,
    status: 'PENDING'
  };

  allPatients : MemberDTO[] = [];
  
  invoices: any[]  = []; // any => formato del listado
  invoicesObjects: InvoiceDTO[]  = [];

  loadingExcel : boolean = false;
  loadingPDF : boolean = false;
  isProcessing : boolean = true;

  exportData: any[];

  actions : ActionDTO[] = [];

  paramsuscription : Subscription;
  idpatient_param : string;

  constructor(
    public toastService: NbToastrService,
    private mediaService : MediaService,
    private pdfService : PdfService,
    private excelService : ExcelService,
    private authService : AuthService,
    private modal: NgbModal, 
    private receiptsService : InvoicesService,
    private usersService : UsersService,
    private router: Router,
    private route: ActivatedRoute

  ) {
   }

  ngOnInit(): void {
    flatpickrFactory();
    this.dayfrom = null;
    this.dayto = null;
    if(!this.canModify()) this.actions = [{action: 'show', text: 'Ver factura'}];
    if(this.canModify()) this.actions = [{action: 'show', text: 'Ver factura'}, {action: 'edit', text: 'Modificar factura'}, {action: 'delete', text: 'Eliminar factura'}];
    
    
    
    this.paramsuscription = this.route.queryParams.subscribe(params => {
      console.log(params)
      this.idpatient_param = params['idpatient'];
      this.getAllPatients(this.idpatient_param);
   });

  }


  roleName(therole) { return rolName(therole); }
  

  
  getAllPatients(idpatientparam :string)
  {
      this.isProcessing = true;
      this.usersService.getAllPatients().subscribe(
        res => {
          this.allPatients = res;
          if(this.allPatients && this.allPatients.length>0) 
          {
            if(idpatientparam) this.idpatient = idpatientparam;
            this.getInvoices(0);
          }
          else
            this.isProcessing = false;
        },
        error => 
        {
          console.error("getAllPatients():"+JSON.stringify(error));
          this.toastService.show("No se pueden obtener los usuarios.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
        }
      );
  }


  getInvoices(page :number) 
  {
    this.isProcessing = true;
    //if(!this.dayfrom) this.dayfrom = null;
    //if(!this.dayto) this.dayto = null;
    this.page = page
    this.receiptsService.getInvoices(this.page, this.size, this.idpatient, this.dayfrom, this.dayto, this.status).subscribe(
      res => {
        this.isProcessing = false;
        this.invoices = res.content.map(item => { return {id: item.id, values: [item.patient_fullname, this.date2Text1(item.duedate), item.total, this.getStatus(item.status), this.date2Text1(item.paiddate), item.url ] }; });
        this.invoicesObjects = res.content;

        this.totalPages = res.totalPages;
      },
      error => {
        this.isProcessing = false;
        console.error("getInvoices():"+JSON.stringify(error));
        this.toastService.show("No se han podido obtener las facturas.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }

  getStatus(status : string)
  {
    if(status=='PAID') return 'PAGADO';
    else if(status=='PENDING') return 'PENDIENTE';
    else return "¿?";
  }

  

  
  getExportData(format: string) 
  {
    if(format === 'excel') this.loadingExcel = true;
    else if(format === 'pdf') this.loadingPDF = true;

    if(!this.dayfrom) this.dayfrom = new Date();
    if(!this.dayto) this.dayto = new Date();

    this.receiptsService.getInvoices(0, 100000000, this.idpatient, this.dayfrom, this.dayto, this.status).subscribe(
      res => {
        const header = {};
        const keys = ['Usuario:', 'Fecha:', 'Total:', 'Estado:', 'Fecha de pago:'];
        const fields = ['patient_fullname', 'duedate', 'total', 'status', 'paiddate'];
        fields.forEach((key, i) => header[key] = keys[i]);
        this.exportData = res && res.content ? res.content.map(item => [item.patient_fullname, this.date2Text1(item.duedate), item.total, this.getStatus(item.status), this.date2Text1(item.paiddate) ]) : null;
        const final = parseDataExport(fields, this.exportData);
        const title = 'Facturas';

        if (format === 'excel') 
        {
          this.excelService.exportAsExcelFile(title, [header].concat(final), title, fields);
          this.loadingExcel = false;
          this.loadingPDF = false;
        } 
        else 
        {
          const minFields = ['30%','20%','10%','10%','10%','20%'];
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
    this.getInvoices(page);
  }


  setPage(event) {
    this.page = event;
    this.getInvoices(this.page);
  }

  action(event) 
  {
    if (event && event[0] === 'edit') 
    {
      const selected = { ...this.invoicesObjects.find(item => item.id === event[1])};
      this.openAddInvoiceModal(selected);
    } 
    else if (event && event[0] === 'delete') 
    {
      this.deleteInvoice(event[1]);
    }
    else if (event && event[0] === 'show') 
    {
      const selected =  {...this.invoicesObjects.find(item => item.id === event[1])};
      window.open(selected.url);
    } 

  }


  canModify() : boolean
  {
    return this.authService.isManager() || this.authService.isAdmin();
  }



  openAddInvoiceModal(selected? : InvoiceDTO)
  {
    console.warn(JSON.stringify(selected));
    if(!selected)
    {
      this.fileName = undefined;
      this.theInvoice.id =''; 
      this.theInvoice.url =''; 
      this.theInvoice.total = 0; 
      this.theInvoice.status ='PENDING';
      this.theInvoice.duedate = new Date();
      this.theInvoice.paiddate = null;
      this.theInvoice.idpatient = this.idpatient;
    }
    else
    {
      this.theInvoice = selected;
      this.theInvoice.duedate = this.localDateTime2Date(this.theInvoice.duedate);
      this.theInvoice.paiddate = this.localDateTime2Date(this.theInvoice.paiddate);
    }



    this.modal.open(this.modalContent, { size: 'lg' });
  }

  saveInvoice()
  {

    
    if(this.theInvoice.idpatient == '') 
      {
        this.toastService.show("Debes seleccionar el usuario primero.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
        );
        return;
      }

    if(this.theInvoice.url == '') 
    {
      this.toastService.show("Debes seleccionar la factura a subir primero.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }


    if(this.theInvoice.total <= 0) 
    {
      this.toastService.show("Debes indicar el total de la factura.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }
   

    this.receiptsService.saveInvoice(this.theInvoice).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Factura grabado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getInvoices(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("saveInvoice():"+JSON.stringify(error));
        this.toastService.show("No se ha podido añadir la factura.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }


  deleteInvoice(id : string)
  {
    this.receiptsService.deleteInvoice(id).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Factura eliminada correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getInvoices(this.page);
      },
      error => {
        this.isProcessing = false;
        console.error("deleteFeeding():"+JSON.stringify(error));
        this.toastService.show("No se ha podido eliminar la factura.",
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
    const upload$ = this.mediaService.uploadFile("invoice", file).pipe(finalize(() => this.reset())
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
            this.theInvoice.url = event.url;
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
            this.theInvoice.url = event.url;
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
