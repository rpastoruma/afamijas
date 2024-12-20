import { Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { NbDialogService, NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import {  parseDataExport, ActionDTO, CountryDTO, StateDTO, CityDTO, PatientDTO } from 'src/app/shared/models/models';
import { PatientsService } from 'src/app/core/services/patients.service';
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
  selector: 'app-users-list',
  templateUrl: './users-list.component.html',
  styleUrls: ['./users-list.component.scss']
})
export class UsersListComponent  implements OnInit{

  

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

  patients: any[]  = []; // amy => formato del listado
  patientsObjects: PatientDTO[]  = [];

  loadingExcel : boolean = false;
  loadingPDF : boolean = false;
  isProcessing : boolean = true;

  exportData: any[];

  actions : ActionDTO[] = [];


  


  thePatient : PatientDTO = {
    id: '',
    username: '',
    email: '',
    name: '',
    surname1: '',
    surname2: '',
    dni: '',
    documenttype: 'DNI',
    phone: '',
    postaladdress: '',
    idcity: undefined,
    cityname: '',
    idstate: undefined,
    statename: '',
    postalcode: '',
    signature: '',
    photo_url: '',
    gender: '',
    idrelative: '',
    relativefullname: '',
    is_principal_keeper: '',
    principal_keeper_fullname: '',
    principal_keeper_phone: '',
    routeDTO: undefined,
    menu_type: '',
    breakfast_description: '',
    medication_description_morning: '',
    medication_description_evening: '',
    groupcode: '',
    birthdate: undefined,
    servicetype: '',
    relativerelation: '',
    idcountry: undefined,
    countryname: '',
    tallerpsico: false,
    transportservice: false,
    transportservice_text: '',
    comedorservice: false,
    comedorservice_text: '',
    ayudadomicilioservice: false,
    ayudadomicilioservice_text: '',
    duchaservice: false,
    duchaservice_text: '',
    register_document_url: '',
    register_document_url_signed: '',
    unregister_document_url: '',
    unregister_document_url_signed: ''
  }



  theCountries : CountryDTO[] = [];
  theStates : StateDTO[] = [];
  theCities : CityDTO[] = [];



  theNewPatient : PatientDTO = { ...this.thePatient };

  constructor(
    public toastService: NbToastrService,
    private patientsService : PatientsService,
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
    this.getPatients(0);
    this.actions = [  {action: 'edit', text: 'Modificar datos del usuario'},  {action: 'show', text: 'Ver registro de pago'},    {action: 'delete', text: 'Dar de baja usuario'}  ];
  }

  getPatients(page? : number)
  {
      this.isProcessing = true;
      if(page) this.page = page;
      this.patientsService.getPatients(this.page, this.size, this.name_surnames, this.documentid, this.status).subscribe(
        res => {
          this.isProcessing = false;
          this.patients = res.content.map(item => { return {id: item.id, values: [item.fullname, item.documentid, item.email, item.phone]  }; });
          this.patientsObjects = res.content;
          this.totalPages = res.totalPages;
        },
        error => 
        {
          this.isProcessing = false;
          console.error("getPatients():"+JSON.stringify(error));
          this.toastService.show("No se pueden obtener los usuarios.",
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
        console.log("this.thePatient.idcountry="  + this.thePatient.idcountry);
        if(!this.thePatient.idcountry) this.thePatient.idcountry = 207; //ESPAÑA
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
    this.frontValuesService.getStates(this.thePatient.idcountry).subscribe(
      res => {
        this.isProcessing = false;
        this.theStates = res;
        if(this.theStates[0]) { if(!this.thePatient.idstate) this.thePatient.idstate = this.theStates[0].id; }
        else this.thePatient.idstate = undefined;
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
    this.frontValuesService.getCities(this.thePatient.idstate).subscribe(
      res => {
        this.isProcessing = false;
        this.theCities = res;
        if(this.theCities[0]) { if(!this.thePatient.idcity) this.thePatient.idcity = this.theCities[0].id; }
        else this.thePatient.idcity = undefined;
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
    if(this.thePatient.idcountry!=207) return; //solo para españa
    this.isProcessing = true;
    this.frontValuesService.getStateAndCitiesByPostalCodeAndCountryId(this.thePatient.postalcode, this.thePatient.idcountry).subscribe(
      res => {
        this.isProcessing = false;
        if(res)
        {
          this.thePatient.idstate = res.state.id;
          this.theCities = res.cities;
          if(this.theCities[0]) this.thePatient.idcity = this.theCities[0].id;
          else this.thePatient.idcity = undefined;
        }
        else
        {
          this.thePatient.idstate = undefined;
          this.thePatient.idcity = undefined;
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

    this.patientsService.getPatients(0, 100000000, this.name_surnames, this.documentid, this.status).subscribe(
      res => {
        const header = {};
        const keys = ['Nombre', 'DNI', 'Email', 'Teléfono'];
        const fields = ['fullname', 'documentid', 'email', 'phone'];
        fields.forEach((key, i) => header[key] = keys[i]);
        this.exportData = res && res.content ? res.content.map(item => [item.fullname, item.documentid, item.email, item.phone]) : null;
        const final = parseDataExport(fields, this.exportData);
        const title = 'Listado de usuarios';

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
    this.getPatients(page);
  }


  setPage(event) {
    this.page = event;
    this.getPatients(this.page);
  }

  action(event) 
  {
    if (event && event[0] === 'show') 
    {
      const selected = this.patientsObjects.find(item => item.id === event[1]);
      this.thePatient = selected;

      window.open('/invoices?idpatient=' + this.thePatient.id);
    } 
    else if (event && event[0] === 'edit') 
      {
        const selected = this.patientsObjects.find(item => item.id === event[1]);
        this.thePatient = selected;
  
        this.openSavePatientModal(false);
      } 
    else if (event && event[0] === 'delete') 
    {
      const selected = this.patientsObjects.find(item => item.id === event[1]);
      this.thePatient = selected;

      this.openUnregisterPatientModal();
    }
  }



  openSavePatientModal(isnew : boolean)
  {
    if(isnew) this.thePatient = {...this.theNewPatient};

    this.getCountries();


    this.modal.open(this.modalContent, { size: 'lg' });
  }

  
  openUnregisterPatientModal()
  {
    this.modal.open(this.modalUnregister, { size: 'lg' });
  }



  
  savePatient()
  {
    
    this.patientsService.savePatient(this.thePatient).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Datos del usuario grabados correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getPatients(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("modifyFood():"+JSON.stringify(error));
        this.toastService.show("No se ha podido grabar el usuario correctamente.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    ); 
  }





  

  unregisterPatient()
  {
    
    this.patientsService.unregisterPatient(this.thePatient.id, this.thePatient.unregister_reason, this.thePatient.unregister_document_url, this.thePatient.unregister_document_url_signed && this.thePatient.unregister_document_url_signed!='').subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Usuario dado de baja correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getPatients(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("modifyFood():"+JSON.stringify(error));
        this.toastService.show("No se ha podido dar de baja el usuario correctamente.",
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


  /*

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
                  this.thePatient.unregister_document_url = event.url;
                else
                  this.thePatient.unregister_document_url_signed = event.url;
            }
            else if(document_type=='ALTA')
            {
                if(this.is_document_signed=='SIN_FIRMA')
                  this.thePatient.register_document_url = event.url;
                else
                  this.thePatient.register_document_url_signed = event.url;
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
                    this.thePatient.unregister_document_url = event.url;
                  else
                    this.thePatient.unregister_document_url_signed = event.url;
              }
                else if(document_type=='ALTA')
              {
                  if(this.is_document_signed=='SIN_FIRMA')
                    this.thePatient.register_document_url = event.url;
                  else
                    this.thePatient.register_document_url_signed = event.url;
              }
          }
          else
            console.error("onFileSelected3():"+JSON.stringify(event));
        }
      }
    )
  }
}
*/




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
    return (this.thePatient.name.trim() + " " + this.thePatient.surname1.trim() + " " + this.thePatient.surname2.trim()).trim();
  }



  openDocumentRegister()
  {
    this.dialogService.open(MyPdfViewerComponent, {
      hasScroll: true,
      closeOnBackdropClick: false,
      closeOnEsc: false,
      context: {
        pdfSrc: this.thePatient.register_document_url,
        fullname : this.getFullName(),
        forSigning : true,
        openExternal : true
      }
    }).onClose.subscribe(
      res => {
          if(res != 'close') 
          {
            if(res.url && res.url.startsWith('https'))
              this.signDocumentRegister(this.thePatient.id, res.url);
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



  signDocumentRegister(idpatient: string, register_document_url_signed : string)
  {
    this.patientsService.signDocumentRegister(idpatient, register_document_url_signed).subscribe(
      res => {
        this.isProcessing = false;
        this.thePatient.register_document_url_signed = register_document_url_signed;
        this.toastService.show("Documento firmado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getPatients(0);
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
        pdfSrc: this.thePatient.unregister_document_url,
        fullname : this.getFullName(),
        forSigning : true,
        openExternal : true
      }
    }).onClose.subscribe(
      res => {
          if(res != 'close') 
          {
            if(res.url && res.url.startsWith('https'))
              this.signDocumentUnRegister(this.thePatient.id, res.url);
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



  signDocumentUnRegister(idpatient: string, unregister_document_url_signed : string)
  {
    this.patientsService.signDocumentUnRegister(idpatient, unregister_document_url_signed).subscribe(
      res => {
        this.isProcessing = false;
        this.thePatient.unregister_document_url_signed = unregister_document_url_signed;
        this.toastService.show("Documento firmado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getPatients(0);
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
    this.thePatient.dni = documentId;

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


  validPostalAddress(thePatient : PatientDTO)
  {
    return thePatient.postalcode && thePatient.postalcode.length>0 && thePatient.postaladdress  && thePatient.postaladdress.length>0 && thePatient.idstate && thePatient.idcity 
  }
  

  validService(thePatient : PatientDTO)
  {
    if(thePatient.servicetype == 'TALLER' || thePatient.servicetype == 'CENTRO_DIA_PRIVADO' || thePatient.servicetype == 'CENTRO_DIA_CONCERTADO') return true;
    else return false;
  }

  validDoc(thePatient : PatientDTO)
  {
      return (thePatient.register_document_url_signed && thePatient.register_document_url_signed.startsWith("https://") ) || (thePatient.register_document_url && thePatient.register_document_url.startsWith("https://"));
  }

}
