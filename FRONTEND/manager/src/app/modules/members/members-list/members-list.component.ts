import { Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import {  parseDataExport, ActionDTO, MemberDTO } from 'src/app/shared/models/models';
import { MembersService } from 'src/app/core/services/members.service';
import { PdfService } from 'src/app/core/services/pdf-service.service';
import { ExcelService } from 'src/app/core/services/excel-service.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-members-list',
  templateUrl: './members-list.component.html',
  styleUrls: ['./members-list.component.scss']
})
export class MembersListComponent  implements OnInit{

  

  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;


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
  
  theMember : MemberDTO = {
    id: '',
    membernumber: '',
    email: '',
    name: '',
    surname1: '',
    surname2: '',
    fullname: '',
    documentid: '',
    documenttype: '',
    phone: '',
    postaladdres: '',
    idcity: '',
    cityname: '',
    idstate: '',
    statename: '',
    postalcode: '',
    fee_euros: 0,
    fee_period: '',
    fee_payment: '',
    bank_name: '',
    bank_account_holder_fullname: '',
    bank_account_holder_dni: '',
    bank_account_iban: '',
    register_document_url: '',
    unregister_document_url: '',
    unregister_reason: ''
  }


  constructor(
    public toastService: NbToastrService,
    private membersService : MembersService,
    private pdfService : PdfService,
    private excelService : ExcelService,
    private authService : AuthService,
    private modal: NgbModal, 

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
      console.log("event=" + JSON.stringify(event));
      console.log("selected=" + JSON.stringify(this.membersObjects));

      const selected = this.membersObjects.find(item => item.id === event[1]);
      this.theMember = selected;

      console.log("selected=" + JSON.stringify(selected));

      this.openMofifyFoodModal();
    } 
  }





  openMofifyFoodModal()
  {

    this.modal.open(this.modalContent, { size: 'lg' });
  }


  /*
  modifyFood()
  {
    this.usersService.modifyFood(this.theFood.idpatient, this.theFood.menu_type, this.theFood.breakfast_description).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Comida modificada correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getFoods(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("modifyFood():"+JSON.stringify(error));
        this.toastService.show("No se ha podido modificar la comida correctamente.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }*/


}
