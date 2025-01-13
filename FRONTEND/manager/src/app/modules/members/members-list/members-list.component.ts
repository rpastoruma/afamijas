import { Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { NbDialogService, NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import {  parseDataExport, ActionDTO, MemberDTO, CountryDTO, StateDTO, CityDTO } from 'src/app/shared/models/models';
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
import { FrontValuesService } from 'src/app/core/services/front-values.service';
import { isPhone } from 'spain-phone';
import * as EmailValidator from 'email-validator';

@Component({
  selector: 'app-members-list',
  templateUrl: './members-list.component.html',
  styleUrls: ['./members-list.component.scss']
})
export class MembersListComponent  implements OnInit{

  

  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;

  @ViewChild('modalUnregister', { static: true }) modalUnregister: TemplateRef<any>;

  requiredFileType:string  =".pdf";
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
    idcity: undefined,
    cityname: '',
    idstate: undefined,
    statename: '',
    idcountry: undefined,
    countryname: '',
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

  theCountries : CountryDTO[] = [];
  theStates : StateDTO[] = [];
  theCities : CityDTO[] = [];



  theNewMember : MemberDTO = { ...this.theMember };

  constructor(
    public toastService: NbToastrService,
    private membersService : MembersService,
    private pdfService : PdfService,
    private excelService : ExcelService,
    private authService : AuthService,
    private modal: NgbModal, 
    private dialogService : NbDialogService,
    private mediaService : MediaService,
    private frontValuesService : FrontValuesService

  )
   {
    }

  ngOnInit(): void {
    this.getMembers(0);
    this.actions = [  {action: 'edit', text: 'Modificar datos de socio'},  {action: 'show', text: 'Ver recibos del socio'},    {action: 'delete', text: 'Dar de baja socio'}  ];
  }

  getMembers(page? : number)
  {
      this.isProcessing = true;
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
          this.isProcessing = false;
          console.error("getMembers():"+JSON.stringify(error));
          this.toastService.show("No se pueden obtener los socios.",
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
        console.log("this.theMember.idcountry="  + this.theMember.idcountry);
        if(!this.theMember.idcountry) this.theMember.idcountry = 207; //ESPAÑA
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
    this.frontValuesService.getStates(this.theMember.idcountry).subscribe(
      res => {
        this.isProcessing = false;
        this.theStates = res;
        if(this.theStates[0]) { if(!this.theMember.idstate) this.theMember.idstate = this.theStates[0].id; }
        else this.theMember.idstate = undefined;
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
    this.frontValuesService.getCities(this.theMember.idstate).subscribe(
      res => {
        this.isProcessing = false;
        this.theCities = res;
        if(this.theCities[0]) { if(!this.theMember.idcity) this.theMember.idcity = this.theCities[0].id; }
        else this.theMember.idcity = undefined;
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
    if(this.theMember.idcountry!=207) return; //solo para españa
    this.isProcessing = true;
    this.frontValuesService.getStateAndCitiesByPostalCodeAndCountryId(this.theMember.postalcode, this.theMember.idcountry).subscribe(
      res => {
        this.isProcessing = false;
        if(res)
        {
          this.theMember.idstate = res.state.id;
          this.theCities = res.cities;
          if(this.theCities[0]) this.theMember.idcity = this.theCities[0].id;
          else this.theMember.idcity = undefined;
        }
        else
        {
          this.theMember.idstate = undefined;
          this.theMember.idcity = undefined;
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
          const minFields = ['10%','30%','20%','20%','20%'];
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
    if (event && event[0] === 'show') 
    {
      const selected = this.membersObjects.find(item => item.id === event[1]);
      this.theMember = selected;

      window.open('/members/receipts?idmember=' + this.theMember.id);
    } 
    else if (event && event[0] === 'edit') 
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

    this.getCountries();


    if(this.theMember.register_document_url_signed && this.theMember.register_document_url_signed.startsWith("https://"))
      this.is_document_signed = 'FIRMADO';
    else
      this.is_document_signed = 'SIN_FIRMA';

    this.modal.open(this.modalContent, { size: 'lg' });
  }

  
  openUnregisterMemberModal()
  {
    if(this.theMember.unregister_document_url_signed && this.theMember.unregister_document_url_signed.startsWith("https://"))
      this.is_document_signed = 'FIRMADO';
    else
      this.is_document_signed = 'SIN_FIRMA';

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
            {
                if(this.is_document_signed=='SIN_FIRMA')
                  this.theMember.unregister_document_url = event.url;
                else
                  this.theMember.unregister_document_url_signed = event.url;
            }
            else if(document_type=='ALTA')
            {
                if(this.is_document_signed=='SIN_FIRMA')
                  this.theMember.register_document_url = event.url;
                else
                  this.theMember.register_document_url_signed = event.url;
            }
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
                {
                  if(this.is_document_signed=='SIN_FIRMA')
                    this.theMember.unregister_document_url = event.url;
                  else
                    this.theMember.unregister_document_url_signed = event.url;
              }
                else if(document_type=='ALTA')
              {
                  if(this.is_document_signed=='SIN_FIRMA')
                    this.theMember.register_document_url = event.url;
                  else
                    this.theMember.register_document_url_signed = event.url;
              }
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


  getFullName()
  {
    return (this.theMember.name.trim() + " " + this.theMember.surname1.trim() + " " + this.theMember.surname2.trim()).trim();
  }



  openDocumentRegister()
  {
    this.dialogService.open(MyPdfViewerComponent, {
      hasScroll: true,
      closeOnBackdropClick: false,
      closeOnEsc: false,
      context: {
        pdfSrc: this.theMember.register_document_url,
        fullname : this.getFullName(),
        forSigning : true,
        openExternal : true
      }
    }).onClose.subscribe(
      res => {
          if(res != 'close') 
          {
            if(res.url && res.url.startsWith('https'))
              this.signDocumentRegister(this.theMember.id, res.url);
            else
            {
              this.toastService.show("No se ha podido firmar el documento correctamente.",
              "¡Ups!", 
              { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
              );
            }
          }
            
      }
    );
  }



  signDocumentRegister(idmember: string, register_document_url_signed : string)
  {
    this.membersService.signDocumentRegister(idmember, register_document_url_signed).subscribe(
      res => {
        this.isProcessing = false;
        this.theMember.register_document_url_signed = register_document_url_signed;
        this.is_document_signed = 'FIRMADO';
        this.toastService.show("Documento firmado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getMembers(0);
      },
      error => {
        this.isProcessing = false;
        console.error("signDocumentRegister():"+JSON.stringify(error));
        this.toastService.show("No se ha podido firmar el documento correctamente.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }



  

  openDocumentUnRegister()
  {
    this.dialogService.open(MyPdfViewerComponent, {
      hasScroll: true,
      closeOnBackdropClick: false,
      closeOnEsc: false,
      context: {
        pdfSrc: this.theMember.unregister_document_url,
        fullname : this.getFullName(),
        forSigning : true,
        openExternal : true
      }
    }).onClose.subscribe(
      res => {
          if(res != 'close') 
          {
            if(res.url && res.url.startsWith('https'))
              this.signDocumentUnRegister(this.theMember.id, res.url);
            else
            {
              this.toastService.show("No se ha podido firmar el documento correctamente.",
              "¡Ups!", 
              { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
              );
            }
          }
            
      }
    );
  }



  signDocumentUnRegister(idmember: string, unregister_document_url_signed : string)
  {
    this.membersService.signDocumentUnRegister(idmember, unregister_document_url_signed).subscribe(
      res => {
        this.isProcessing = false;
        this.theMember.unregister_document_url_signed = unregister_document_url_signed;
        this.is_document_signed = 'FIRMADO';
        this.toastService.show("Documento firmado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getMembers(0);
      },
      error => {
        this.isProcessing = false;
        console.error("signDocumentUnRegister():"+JSON.stringify(error));
        this.toastService.show("No se ha podido firmar el documento correctamente.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }




  validEmail(email :string)
  {
    return EmailValidator.validate(email);
  }
  
  validPhone(phone : string)
  {
    return isPhone(phone);
  }


  validDocumentId(documentId: string, documentType: string)
  {
    documentId = documentId.toUpperCase();
    this.theMember.documentid = documentId;

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


  validPostalAddress(theMember : MemberDTO)
  {
    return theMember.postalcode && theMember.postalcode.length>0 && theMember.postaladdress  && theMember.postaladdress.length>0 && theMember.idstate && theMember.idcity 
  }
  

  validFee(theMember : MemberDTO)
  {
    /*TODO: REHACER PQ DABA ERROR EN CONSOLA
    if(theMember.fee_payment == 'SEDE')
    {
      return theMember.fee_euros > 0 && theMember.fee_period.length > 0;
    }
    else if(theMember.fee_payment == 'DOMICILIACIÓN')
    {
      return theMember.fee_euros > 0 && theMember.fee_period.length > 0 && theMember.bank_account_holder_dni.length > 0 && theMember.bank_account_holder_fullname.length > 0 && theMember.bank_account_iban.length > 0 && theMember.bank_name.length > 0;
    }*/ return true;
  }

  validDoc(theMember : MemberDTO)
  {
    if(this.is_document_signed)
    {
      return theMember.register_document_url_signed && theMember.register_document_url_signed.startsWith("https://");
    }
    else
    {
      return theMember.register_document_url && theMember.register_document_url.startsWith("https://");
    }
  }

}
