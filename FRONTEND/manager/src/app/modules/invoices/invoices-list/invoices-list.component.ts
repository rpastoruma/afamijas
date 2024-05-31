import { Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import { parseDataExport, ActionDTO, rolName, ReceiptDTO, MemberDTO } from 'src/app/shared/models/models';
import { UsersService } from 'src/app/core/services/users.service';
import { PdfService } from 'src/app/core/services/pdf-service.service';
import { ExcelService } from 'src/app/core/services/excel-service.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { flatpickrFactory } from '../../calendar/mycalendar.module'; 
import { Subscription, finalize } from 'rxjs';
import { MediaService } from 'src/app/core/services/media.service';
import { HttpEventType } from '@angular/common/http';
import { ReceiptsService } from 'src/app/core/services/receipts.service';
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
  idmember : string = '';
  status : string = '';

  page : number = 0;
  size : number = 4;
  totalPages : number = 0;

  
  requiredFileType:string  = ".png,.jpg,.jpeg,.webp,.gif,.pdf,.doc,.docx,.xsl,.xslx";
  fileName = '';
  uploadProgress:number;
  uploadSub: Subscription;

  theReceipt : ReceiptDTO = {
    id: '',
    idmember: '',
    member_fullname: '',
    total: 0,
    url: '',
    duedate: undefined,
    paiddate: undefined,
    status: 'PENDING'
  };

  allMembers : MemberDTO[] = [];
  
  receipts: any[]  = []; // any => formato del listado
  receiptsObjects: ReceiptDTO[]  = [];

  loadingExcel : boolean = false;
  loadingPDF : boolean = false;
  isProcessing : boolean = true;

  exportData: any[];

  actions : ActionDTO[] = [];

  paramsuscription : Subscription;
  idmember_param : string;

  constructor(
    public toastService: NbToastrService,
    private mediaService : MediaService,
    private pdfService : PdfService,
    private excelService : ExcelService,
    private authService : AuthService,
    private modal: NgbModal, 
    private receiptsService : ReceiptsService,
    private usersService : UsersService,
    private router: Router,
    private route: ActivatedRoute

  ) {
   }

  ngOnInit(): void {
    flatpickrFactory();
    this.dayfrom = null;
    this.dayto = null;
    if(!this.canModify()) this.actions = [{action: 'show', text: 'Ver recibo'}];
    if(this.canModify()) this.actions = [{action: 'show', text: 'Ver recibo'}, {action: 'edit', text: 'Modificar recibo'}, {action: 'delete', text: 'Eliminar recibo'}];
    
    
    
    this.paramsuscription = this.route.queryParams.subscribe(params => {
      console.log(params)
      this.idmember_param = params['idmember'];
      this.getAllMembers(this.idmember_param);
   });

  }


  roleName(therole) { return rolName(therole); }
  

  
  getAllMembers(idmemberparam :string)
  {
      this.isProcessing = true;
      this.usersService.getAllMembers().subscribe(
        res => {
          this.allMembers = res;
          if(this.allMembers && this.allMembers.length>0) 
          {
            if(idmemberparam) this.idmember = idmemberparam;
            this.getReceipts(0);
          }
          else
            this.isProcessing = false;
        },
        error => 
        {
          console.error("getAllMembers():"+JSON.stringify(error));
          this.toastService.show("No se pueden obtener los socios.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
        }
      );
  }


  getReceipts(page :number) 
  {
    this.isProcessing = true;
    //if(!this.dayfrom) this.dayfrom = null;
    //if(!this.dayto) this.dayto = null;
    this.page = page
    this.receiptsService.getReceipts(this.page, this.size, this.idmember, this.dayfrom, this.dayto, this.status).subscribe(
      res => {
        this.isProcessing = false;
        this.receipts = res.content.map(item => { return {id: item.id, values: [item.member_fullname, this.date2Text1(item.duedate), item.total, this.getStatus(item.status), this.date2Text1(item.paiddate), item.url ] }; });
        this.receiptsObjects = res.content;

        this.totalPages = res.totalPages;
      },
      error => {
        this.isProcessing = false;
        console.error("getReceipts():"+JSON.stringify(error));
        this.toastService.show("No se han podido obtener los recibos.",
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

    this.receiptsService.getReceipts(0, 100000000, this.idmember, this.dayfrom, this.dayto, this.status).subscribe(
      res => {
        const header = {};
        const keys = ['Socio:', 'Fecha:', 'Total:', 'Estado:', 'Fecha de pago:'];
        const fields = ['member_fullname', 'duedate', 'total', 'status', 'paiddate'];
        fields.forEach((key, i) => header[key] = keys[i]);
        this.exportData = res && res.content ? res.content.map(item => [item.member_fullname, this.date2Text1(item.duedate), item.total, this.getStatus(item.status), this.date2Text1(item.paiddate) ]) : null;
        const final = parseDataExport(fields, this.exportData);
        const title = 'Recibos';

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
    this.getReceipts(page);
  }


  setPage(event) {
    this.page = event;
    this.getReceipts(this.page);
  }

  action(event) 
  {
    if (event && event[0] === 'edit') 
    {
      const selected = { ...this.receiptsObjects.find(item => item.id === event[1])};
      this.openAddReceiptModal(selected);
    } 
    else if (event && event[0] === 'delete') 
    {
      this.deleteReceipt(event[1]);
    }
    else if (event && event[0] === 'show') 
    {
      const selected =  {...this.receiptsObjects.find(item => item.id === event[1])};
      window.open(selected.url);
    } 

  }


  canModify() : boolean
  {
    return this.authService.isManager() || this.authService.isAdmin();
  }



  openAddReceiptModal(selected? : ReceiptDTO)
  {
    console.warn(JSON.stringify(selected));
    if(!selected)
    {
      this.fileName = undefined;
      this.theReceipt.id =''; 
      this.theReceipt.url =''; 
      this.theReceipt.total = 0; 
      this.theReceipt.status ='PENDING';
      this.theReceipt.duedate = new Date();
      this.theReceipt.paiddate = null;
      this.theReceipt.idmember = this.idmember;
    }
    else
    {
      this.theReceipt = selected;
      this.theReceipt.duedate = this.localDateTime2Date(this.theReceipt.duedate);
      this.theReceipt.paiddate = this.localDateTime2Date(this.theReceipt.paiddate);
    }



    this.modal.open(this.modalContent, { size: 'lg' });
  }

  saveReceipt()
  {

    
    if(this.theReceipt.idmember == '') 
      {
        this.toastService.show("Debes seleccionar el socio primero.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
        );
        return;
      }

    if(this.theReceipt.url == '') 
    {
      this.toastService.show("Debes seleccionar el recibo a subir primero.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }


    if(this.theReceipt.total <= 0) 
    {
      this.toastService.show("Debes indicar el total del recibo.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }
   

    this.receiptsService.saveReceipt(this.theReceipt).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Recibo grabado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getReceipts(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("saveReceipt():"+JSON.stringify(error));
        this.toastService.show("No se ha podido añadir el recibo.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }


  deleteReceipt(id : string)
  {
    this.receiptsService.deleteReceipt(id).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Recibo eliminado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getReceipts(this.page);
      },
      error => {
        this.isProcessing = false;
        console.error("deleteFeeding():"+JSON.stringify(error));
        this.toastService.show("No se ha podido eliminar el recibo.",
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
    const upload$ = this.mediaService.uploadFile("recepit", file).pipe(finalize(() => this.reset())
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
            this.theReceipt.url = event.url;
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
            this.theReceipt.url = event.url;
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
