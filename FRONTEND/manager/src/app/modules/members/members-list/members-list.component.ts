import { Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { NbDialogService, NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import {  parseDataExport, ActionDTO, MemberDTO } from 'src/app/shared/models/models';
import { MembersService } from 'src/app/core/services/members.service';
import { PdfService } from 'src/app/core/services/pdf-service.service';
import { ExcelService } from 'src/app/core/services/excel-service.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DeleteConfirmComponent } from 'src/app/shared/components/delete-confirm/delete-confirm.component';
import { Subscription, finalize } from 'rxjs';
import { HttpEventType } from '@angular/common/http';
import { MediaService } from 'src/app/core/services/media.service';
import { MyPdfViewerComponent } from 'src/app/shared/components/my-pdf-viewer/my-pdf-viewer.component';

@Component({
  selector: 'app-members-list',
  templateUrl: './members-list.component.html',
  styleUrls: ['./members-list.component.scss']
})
export class MembersListComponent  implements OnInit{

  

  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;

  @ViewChild('modalUnregister', { static: true }) modalUnregister: TemplateRef<any>;

  requiredFileType:string  = ".png,.jpg,.jpeg,.webp,.gif,.pdf,.doc,.docx,.xsl,.xslx";
  fileName = '';
  uploadProgress:number;
  uploadSub: Subscription;


  page : number = 0;
  size : number = 5;
  totalPages : number = 0;

  membernumber : number;
  name_surnames : string = '';
  documentid : string;
  status : string = 'A';

  members: any[]  = []; // amy => formato del listado
  membersObjects: MemberDTO[]  = [];

  loadingExcel : boolean = false;
  loadingPDF : boolean = false;
  isProcessing : boolean = true;

  exportData: any[];

  actions : ActionDTO[] = [];

  allStates : any[] = [];
  allCities : any[] = [];
  

  is_document_signed : string = "SIN_FIRMA";

  theMember : MemberDTO = {
    id: '',
    membernumber: '',
    email: '',
    name: '',
    surname1: '',
    surname2: '',
    fullname: '',
    documentid: '',
    documenttype: 'DNI',
    phone: '',
    postaladdress: '',
    idcity: '',
    cityname: '',
    idstate: '',
    statename: '',
    postalcode: '',
    fee_euros: 0,
    fee_period: '',
    fee_payment: 'SEDE',
    bank_name: '',
    bank_account_holder_fullname: '',
    bank_account_holder_dni: '',
    bank_account_iban: '',
    register_document_url: '',
    register_document_url_signed: '',
    unregister_document_url: '',
    unregister_document_url_signed: '',
    unregister_reason: '',
    status : 'A'
  }

  theNewMember : MemberDTO = { ...this.theMember };

  constructor(
    public toastService: NbToastrService,
    private membersService : MembersService,
    private pdfService : PdfService,
    private excelService : ExcelService,
    private authService : AuthService,
    private modal: NgbModal, 
    private dialogService : NbDialogService,
    private mediaService : MediaService

  ) { }

  ngOnInit(): void {
    this.getMembers(0);
    this.actions = [  {action: 'edit', text: 'Modificar datos de socio'},  {action: 'delete', text: 'Dar de baja socio'}  ];
  }

  getMembers(page? : number)
  {
      if(page) this.page = page;
      this.membersService.getMembers(this.page, this.size, this.membernumber, this.name_surnames, this.documentid, this.status).subscribe(
        res => {

          this.isProcessing = false;
          this.members = res.content.map(item => { return {id: item.id, values: [item.membernumber, item.fullname, item.documentid, item.email, item.phone]  }; });
          this.membersObjects = res.content;
          this.totalPages = res.totalPages;
        },
        error => 
        {
          console.error("getPatients():"+JSON.stringify(error));
          this.toastService.show("No se pueden obtener los socios.",
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

    this.membersService.getMembers(0, 100000000, this.membernumber, this.name_surnames, this.documentid, this.status).subscribe(
      res => {
        const header = {};
        const keys = ['Nº Socio', 'Nombre', 'DNI', 'Email', 'Teléfono'];
        const fields = ['membernumber', 'fullname', 'documentid', 'email', 'phone'];
        fields.forEach((key, i) => header[key] = keys[i]);
        this.exportData = res && res.content ? res.content.map(item => [item.membernumber, item.fullname, item.documentid, item.email, item.phone]) : null;
        const final = parseDataExport(fields, this.exportData);
        const title = 'Listado de socios';

        if (format === 'excel') 
        {
          this.excelService.exportAsExcelFile(title, [header].concat(final), title, fields);
          this.loadingExcel = false;
          this.loadingPDF = false;
        } 
        else 
        {
          const minFields = ['34%','33%','33%'];
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
    this.getMembers(page);
  }


  setPage(event) {
    this.page = event;
    this.getMembers(this.page);
  }

  action(event) 
  {
    if (event && event[0] === 'edit') 
    {
      const selected = this.membersObjects.find(item => item.id === event[1]);
      this.theMember = selected;

      this.openSaveMemberModal(false);
    } 
    else if (event && event[0] === 'delete') 
    {
      const selected = this.membersObjects.find(item => item.id === event[1]);
      this.theMember = selected;

      this.openUnregisterMemberModal();
    }
  }



  openSaveMemberModal(isnew : boolean)
  {
    if(isnew) this.theMember = {...this.theNewMember};

    this.modal.open(this.modalContent, { size: 'lg' });
  }

  
  openUnregisterMemberModal()
  {

    console.log("openUnregisterMemberModal= " + JSON.stringify(this.theMember));
    this.modal.open(this.modalUnregister, { size: 'lg' });
  }



  
  saveMember()
  {
    
    this.membersService.saveMember(this.theMember, this.is_document_signed=='FIRMADO').subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Datos del socio grabados correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getMembers(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("modifyFood():"+JSON.stringify(error));
        this.toastService.show("No se ha podido grabar el socio correctamente.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    ); 
  }






  

  unregisterMember()
  {
    
    this.membersService.unregisterMember(this.theMember.id, this.theMember.unregister_reason, this.theMember.unregister_document_url, this.is_document_signed=='FIRMADO').subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Socio dado de baja correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getMembers(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("modifyFood():"+JSON.stringify(error));
        this.toastService.show("No se ha podido dar de baja el socio correctamente.",
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
    document.getElementById('step4').style.display = 'none';

    document.getElementById(step).style.display = 'block';
  }

async onFileSelected(event, document_type) {

  const file:File = event.target.files[0];

  if (file) 
  {
    
    const upload$ = this.mediaService.uploadFile(document_type=='ALTA'?"register_document":"unregister_document", file).pipe(finalize(() => this.reset())
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
            if(document_type=='BAJA')
              this.theMember.unregister_document_url = event.url;
            else if(document_type=='ALTA')
              this.theMember.register_document_url = event.url;
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
          {
              if(document_type=='BAJA')
                this.theMember.unregister_document_url = event.url;
              else if(document_type=='ALTA')
                this.theMember.register_document_url = event.url;
          }
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

}
