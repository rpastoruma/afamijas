import { Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { NbDialogService, NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import {  parseDataExport, ActionDTO, VolunteerDTO, CountryDTO, StateDTO, CityDTO } from 'src/app/shared/models/models';
import { VolunteersService } from 'src/app/core/services/volunteers.service';
import { PdfService } from 'src/app/core/services/pdf-service.service';
import { ExcelService } from 'src/app/core/services/excel-service.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subscription, finalize } from 'rxjs';
import { HttpEventType } from '@angular/common/http';
import { MediaService } from 'src/app/core/services/media.service';
import { MyPdfViewerComponent } from 'src/app/shared/components/my-pdf-viewer/my-pdf-viewer.component';
import { FrontValuesService } from 'src/app/core/services/front-values.service';
import { isPhone } from 'spain-phone';
import * as EmailValidator from 'email-validator';

@Component({
  selector: 'app-volunteers-list',
  templateUrl: './volunteers-list.component.html',
  styleUrls: ['./volunteers-list.component.scss']
})
export class VolunteersListComponent  implements OnInit{

  

  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;

  @ViewChild('modalUnregister', { static: true }) modalUnregister: TemplateRef<any>;

  requiredFileType:string  =".pdf";
  fileName = '';
  uploadProgress:number;
  uploadSub: Subscription;


  page : number = 0;
  size : number = 5;
  totalPages : number = 0;

  name_surnames : string = '';
  documentid : string;
  status : string = 'A';

  volunteers: any[]  = []; // amy => formato del listado
  volunteersObjects: VolunteerDTO[]  = [];

  loadingExcel : boolean = false;
  loadingPDF : boolean = false;
  isProcessing : boolean = true;

  exportData: any[];

  actions : ActionDTO[] = [];


  

  is_document_signed : string = "SIN_FIRMA";

  theVolunteer : VolunteerDTO = {
    id: '',
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



  theNewVolunteer : VolunteerDTO = { ...this.theVolunteer };

  constructor(
    public toastService: NbToastrService,
    private volunteersService : VolunteersService,
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
    this.getVolunteers(0);
    this.actions = [  {action: 'edit', text: 'Modificar datos de voluntario'},    {action: 'delete', text: 'Dar de baja voluntario'}  ];
  }

  getVolunteers(page? : number)
  {
      this.isProcessing = true;
      if(page) this.page = page;
      this.volunteersService.getVolunteers(this.page, this.size, this.name_surnames, this.documentid, this.status).subscribe(
        res => {
          this.isProcessing = false;
          this.volunteers = res.content.map(item => { return {id: item.id, values: [item.fullname, item.documentid, item.email, item.phone]  }; });
          this.volunteersObjects = res.content;
          this.totalPages = res.totalPages;
        },
        error => 
        {
          this.isProcessing = false;
          console.error("getVolunteers():"+JSON.stringify(error));
          this.toastService.show("No se pueden obtener los voluntarios.",
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
        console.log("this.theVolunteer.idcountry="  + this.theVolunteer.idcountry);
        if(!this.theVolunteer.idcountry) this.theVolunteer.idcountry = 207; //ESPAÑA
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
    this.frontValuesService.getStates(this.theVolunteer.idcountry).subscribe(
      res => {
        this.isProcessing = false;
        this.theStates = res;
        if(this.theStates[0]) { if(!this.theVolunteer.idstate) this.theVolunteer.idstate = this.theStates[0].id; }
        else this.theVolunteer.idstate = undefined;
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
    this.frontValuesService.getCities(this.theVolunteer.idstate).subscribe(
      res => {
        this.isProcessing = false;
        this.theCities = res;
        if(this.theCities[0]) { if(!this.theVolunteer.idcity) this.theVolunteer.idcity = this.theCities[0].id; }
        else this.theVolunteer.idcity = undefined;
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
    if(this.theVolunteer.idcountry!=207) return; //solo para españa
    this.isProcessing = true;
    this.frontValuesService.getStateAndCitiesByPostalCodeAndCountryId(this.theVolunteer.postalcode, this.theVolunteer.idcountry).subscribe(
      res => {
        this.isProcessing = false;
        if(res)
        {
          this.theVolunteer.idstate = res.state.id;
          this.theCities = res.cities;
          if(this.theCities[0]) this.theVolunteer.idcity = this.theCities[0].id;
          else this.theVolunteer.idcity = undefined;
        }
        else
        {
          this.theVolunteer.idstate = undefined;
          this.theVolunteer.idcity = undefined;
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

    this.volunteersService.getVolunteers(0, 100000000, this.name_surnames, this.documentid, this.status).subscribe(
      res => {
        const header = {};
        const keys = ['Nombre', 'DNI', 'Email', 'Teléfono'];
        const fields = ['fullname', 'documentid', 'email', 'phone'];
        fields.forEach((key, i) => header[key] = keys[i]);
        this.exportData = res && res.content ? res.content.map(item => [item.fullname, item.documentid, item.email, item.phone]) : null;
        const final = parseDataExport(fields, this.exportData);
        const title = 'Listado de voluntarios';

        if (format === 'excel') 
        {
          this.excelService.exportAsExcelFile(title, [header].concat(final), title, fields);
          this.loadingExcel = false;
          this.loadingPDF = false;
        } 
        else 
        {
          const minFields = ['40%','20%','20%','20%'];
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
    this.getVolunteers(page);
  }


  setPage(event) {
    this.page = event;
    this.getVolunteers(this.page);
  }

  action(event) 
  {
    if (event && event[0] === 'edit') 
      {
        const selected = this.volunteersObjects.find(item => item.id === event[1]);
        this.theVolunteer = selected;
  
        this.openSaveVolunteerModal(false);
      } 
    else if (event && event[0] === 'delete') 
    {
      const selected = this.volunteersObjects.find(item => item.id === event[1]);
      this.theVolunteer = selected;

      this.openUnregisterVolunteerModal();
    }
  }



  openSaveVolunteerModal(isnew : boolean)
  {
    if(isnew) this.theVolunteer = {...this.theNewVolunteer};

    this.getCountries();


    if(this.theVolunteer.register_document_url_signed && this.theVolunteer.register_document_url_signed.startsWith("https://"))
      this.is_document_signed = 'FIRMADO';
    else
      this.is_document_signed = 'SIN_FIRMA';

    this.modal.open(this.modalContent, { size: 'lg' });
  }


  openUnregisterVolunteerModal()
  {
    if(this.theVolunteer.unregister_document_url_signed && this.theVolunteer.unregister_document_url_signed.startsWith("https://"))
      this.is_document_signed = 'FIRMADO';
    else
      this.is_document_signed = 'SIN_FIRMA';

    this.modal.open(this.modalUnregister, { size: 'lg' });
  }



  
  saveVolunteer()
  {
    
    this.volunteersService.saveVolunteer(this.theVolunteer, this.is_document_signed=='FIRMADO').subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Datos del voluntario grabados correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getVolunteers(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("saveVolunteer():"+JSON.stringify(error));
        this.toastService.show("No se ha podido grabar el voluntario correctamente.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    ); 
  }





  

  unregisterVolunteer()
  {
    
    this.volunteersService.unregisterVolunteer(this.theVolunteer.id, this.theVolunteer.unregister_reason, this.theVolunteer.unregister_document_url, this.is_document_signed=='FIRMADO').subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Voluntario dado de baja correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getVolunteers(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("unregisterVolunteer():"+JSON.stringify(error));
        this.toastService.show("No se ha podido dar de baja el voluntario correctamente.",
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
                  this.theVolunteer.unregister_document_url = event.url;
                else
                  this.theVolunteer.unregister_document_url_signed = event.url;
            }
            else if(document_type=='ALTA')
            {
                if(this.is_document_signed=='SIN_FIRMA')
                  this.theVolunteer.register_document_url = event.url;
                else
                  this.theVolunteer.register_document_url_signed = event.url;
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
                    this.theVolunteer.unregister_document_url = event.url;
                  else
                    this.theVolunteer.unregister_document_url_signed = event.url;
              }
                else if(document_type=='ALTA')
              {
                  if(this.is_document_signed=='SIN_FIRMA')
                    this.theVolunteer.register_document_url = event.url;
                  else
                    this.theVolunteer.register_document_url_signed = event.url;
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
    return (this.theVolunteer.name.trim() + " " + this.theVolunteer.surname1.trim() + " " + this.theVolunteer.surname2.trim()).trim();
  }



  openDocumentRegister()
  {
    this.dialogService.open(MyPdfViewerComponent, {
      hasScroll: true,
      closeOnBackdropClick: false,
      closeOnEsc: false,
      context: {
        pdfSrc: this.theVolunteer.register_document_url,
        fullname : this.getFullName(),
        forSigning : true,
        openExternal : true
      }
    }).onClose.subscribe(
      res => {
          if(res != 'close') 
          {
            if(res.url && res.url.startsWith('https'))
              this.signDocumentRegister(this.theVolunteer.id, res.url);
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



  signDocumentRegister(idvolunteer: string, register_document_url_signed : string)
  {
    this.volunteersService.signDocumentRegister(idvolunteer, register_document_url_signed).subscribe(
      res => {
        this.isProcessing = false;
        this.theVolunteer.register_document_url_signed = register_document_url_signed;
        this.is_document_signed = 'FIRMADO';
        this.toastService.show("Documento firmado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getVolunteers(0);
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
        pdfSrc: this.theVolunteer.unregister_document_url,
        fullname : this.getFullName(),
        forSigning : true,
        openExternal : true
      }
    }).onClose.subscribe(
      res => {
          if(res != 'close') 
          {
            if(res.url && res.url.startsWith('https'))
              this.signDocumentUnRegister(this.theVolunteer.id, res.url);
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



  signDocumentUnRegister(idvolunteer: string, unregister_document_url_signed : string)
  {
    this.volunteersService.signDocumentUnRegister(idvolunteer, unregister_document_url_signed).subscribe(
      res => {
        this.isProcessing = false;
        this.theVolunteer.unregister_document_url_signed = unregister_document_url_signed;
        this.is_document_signed = 'FIRMADO';
        this.toastService.show("Documento firmado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getVolunteers(0);
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
    if(documentId == null || documentId.length == 0) return false;
    if(documentType == null || documentType.length == 0) return false;
    
    documentId = documentId.toUpperCase();
    this.theVolunteer.documentid = documentId;

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


  validPostalAddress(theVolunteer : VolunteerDTO)
  {
    return theVolunteer.postalcode && theVolunteer.postalcode.length>0 && theVolunteer.postaladdress  && theVolunteer.postaladdress.length>0 && theVolunteer.idstate && theVolunteer.idcity 
  }
  

  validFee(theVolunteer : VolunteerDTO)
  {
    /*TODO: REHACER PQ DABA ERROR EN CONSOLA
    if(theVolunteer.fee_payment == 'SEDE')
    {
      return theVolunteer.fee_euros > 0 && theVolunteer.fee_period.length > 0;
    }
    else if(theVolunteer.fee_payment == 'DOMICILIACIÓN')
    {
      return theVolunteer.fee_euros > 0 && theVolunteer.fee_period.length > 0 && theVolunteer.bank_account_holder_dni.length > 0 && theVolunteer.bank_account_holder_fullname.length > 0 && theVolunteer.bank_account_iban.length > 0 && theVolunteer.bank_name.length > 0;
    }*/ return true;
  }

  validDoc(theVolunteer : VolunteerDTO)
  {
    if(this.is_document_signed)
    {
      return theVolunteer.register_document_url_signed && theVolunteer.register_document_url_signed.startsWith("https://");
    }
    else
    {
      return theVolunteer.register_document_url && theVolunteer.register_document_url.startsWith("https://");
    }
  }

}
