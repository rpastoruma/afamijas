
import { Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { NbDialogService, NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import {  parseDataExport, ActionDTO, RelativeDTO, CountryDTO, StateDTO, CityDTO } from 'src/app/shared/models/models';
import { MembersService } from 'src/app/core/services/members.service';
import { PdfService } from 'src/app/core/services/pdf-service.service';
import { ExcelService } from 'src/app/core/services/excel-service.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { MediaService } from 'src/app/core/services/media.service';
import { FrontValuesService } from 'src/app/core/services/front-values.service';
import { isPhone } from 'spain-phone';
import * as EmailValidator from 'email-validator';

@Component({
  selector: 'app-relatives-list',
  templateUrl: './relatives-list.component.html',
  styleUrls: ['./relatives-list.component.scss']
})
export class RelativesListComponent  implements OnInit{

  

  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;

  @ViewChild('modalUnregister', { static: true }) modalUnregister: TemplateRef<any>;



  page : number = 0;
  size : number = 5;
  totalPages : number = 0;

  name_surnames : string = '';
  documentid : string;
  status : string = 'A';

  relatives: any[]  = []; // amy => formato del listado
  relativesObjects: RelativeDTO[]  = [];

  loadingExcel : boolean = false;
  loadingPDF : boolean = false;
  isProcessing : boolean = true;

  exportData: any[];

  actions : ActionDTO[] = [];



  theRelative : RelativeDTO = {
    id: '',
    username: '',
    email: '',
    name: '',
    surname1: '',
    surname2: '',
    documentid: '',
    fullname: '',
    idcity: 0,
    idstate: 0,
    idcountry: 0,
    cityname: '',
    statename: '',
    countryname: '',
    postalcode: '',
    postaladdress: '',
    documenttype: '',
    phone: ''
  }

  theCountries : CountryDTO[] = [];
  theStates : StateDTO[] = [];
  theCities : CityDTO[] = [];



  theNewRelative : RelativeDTO = { ...this.theRelative };

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
    this.getRelatives(0);
    this.actions = [  {action: 'edit', text: 'Modificar datos de familiar'},   {action: 'delete', text: 'Dar de baja familiar'}  ];
  }

  getRelatives(page? : number)
  {
      this.isProcessing = true;
      if(page) this.page = page;
      this.membersService.getRelatives(this.page, this.size, this.name_surnames, this.documentid, this.status).subscribe(
        res => {
          this.isProcessing = false;
          this.relatives = res.content.map(item => { return {id: item.id, values: [item.fullname, item.documentid, item.email, item.phone]  }; });
          this.relativesObjects = res.content;
          this.totalPages = res.totalPages;
        },
        error => 
        {
          this.isProcessing = false;
          console.error("getMembers():"+JSON.stringify(error));
          this.toastService.show("No se pueden obtener los familiares.",
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
        console.log("this.theRelative.idcountry="  + this.theRelative.idcountry);
        if(!this.theRelative.idcountry) this.theRelative.idcountry = 207; //ESPAÑA
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
    this.frontValuesService.getStates(this.theRelative.idcountry).subscribe(
      res => {
        this.isProcessing = false;
        this.theStates = res;
        if(this.theStates[0]) { if(!this.theRelative.idstate) this.theRelative.idstate = this.theStates[0].id; }
        else this.theRelative.idstate = undefined;
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
    this.frontValuesService.getCities(this.theRelative.idstate).subscribe(
      res => {
        this.isProcessing = false;
        this.theCities = res;
        if(this.theCities[0]) { if(!this.theRelative.idcity) this.theRelative.idcity = this.theCities[0].id; }
        else this.theRelative.idcity = undefined;
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
    if(this.theRelative.idcountry!=207) return; //solo para españa
    this.isProcessing = true;
    this.frontValuesService.getStateAndCitiesByPostalCodeAndCountryId(this.theRelative.postalcode, this.theRelative.idcountry).subscribe(
      res => {
        this.isProcessing = false;
        if(res)
        {
          this.theRelative.idstate = res.state.id;
          this.theCities = res.cities;
          if(this.theCities[0]) this.theRelative.idcity = this.theCities[0].id;
          else this.theRelative.idcity = undefined;
        }
        else
        {
          this.theRelative.idstate = undefined;
          this.theRelative.idcity = undefined;
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

    this.membersService.getRelatives(0, 100000000, this.name_surnames, this.documentid, this.status).subscribe(
      res => {
        const header = {};
        const keys = ['Nombre', 'DNI', 'Email', 'Teléfono'];
        const fields = ['fullname', 'documentid', 'email', 'phone'];
        fields.forEach((key, i) => header[key] = keys[i]);
        this.exportData = res && res.content ? res.content.map(item => [item.fullname, item.documentid, item.email, item.phone]) : null;
        const final = parseDataExport(fields, this.exportData);
        const title = 'Listado de familiares';

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
    this.getRelatives(page);
  }


  setPage(event) {
    this.page = event;
    this.getRelatives(this.page);
  }

  action(event) 
  {
    if (event && event[0] === 'edit') 
      {
        const selected = this.relativesObjects.find(item => item.id === event[1]);
        this.theRelative = selected;
  
        this.openSaveRelativeModal(false);
      } 
    else if (event && event[0] === 'delete') 
    {
      const selected = this.relativesObjects.find(item => item.id === event[1]);
      this.theRelative = selected;

      this.deleteRelativeModal();
    }
  }



  openSaveRelativeModal(isnew : boolean)
  {
    if(isnew) this.theRelative = {...this.theNewRelative};

    this.getCountries();



    this.modal.open(this.modalContent, { size: 'lg' });
  }

  
  deleteRelativeModal()
  {

    this.modal.open(this.modalUnregister, { size: 'lg' });
  }



  
  saveRelative()
  {
    
    this.membersService.saveRelative(this.theRelative).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Datos del familiar grabados correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getRelatives(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("modifyFood():"+JSON.stringify(error));
        this.toastService.show("No se ha podido grabar el familiar correctamente.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    ); 
  }





  

  deleteRelative()
  {
    
    this.membersService.deleteRelative(this.theRelative.id).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Familiar dado de baja correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getRelatives(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("modifyFood():"+JSON.stringify(error));
        this.toastService.show("No se ha podido dar de baja el familiar correctamente.",
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

    document.getElementById(step).style.display = 'block';
  }


  getFullName()
  {
    return (this.theRelative.name.trim() + " " + this.theRelative.surname1.trim() + " " + this.theRelative.surname2.trim()).trim();
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
    this.theRelative.documentid = documentId;

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


  validPostalAddress(theRelative : RelativeDTO)
  {
    return theRelative.postalcode && theRelative.postalcode.length>0 && theRelative.postaladdress  && theRelative.postaladdress.length>0 && theRelative.idstate && theRelative.idcity 
  }
  

}
