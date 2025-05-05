import { Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import { parseDataExport, ActionDTO, rolName, AddressBookDTO } from 'src/app/shared/models/models';
import { UsersService } from 'src/app/core/services/users.service';
import { PdfService } from 'src/app/core/services/pdf-service.service';
import { ExcelService } from 'src/app/core/services/excel-service.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { flatpickrFactory } from '../../calendar/mycalendar.module'; 
import { finalize } from 'rxjs';
import { MediaService } from 'src/app/core/services/media.service';
import { HttpEventType } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-agenda-list',
  templateUrl: './agenda-list.component.html',
  styleUrls: ['./agenda-list.component.scss']
})
export class AgendaListComponent  implements OnInit {

  

  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;

  //PARÁMETROS LISTADO
  type : string = '';
  fullname : string = '';
  phone : string = '';
  email : string = '';

  page : number = 0;
  size : number = 4;
  totalPages : number = 0;

  selectedContact : AddressBookDTO = {
    id: '',
    type: '',
    fullname: ''
  };


  
  contacts: any[]  = []; // any => formato del listado
  contactsObjects: AddressBookDTO[]  = [];

  loadingExcel : boolean = false;
  loadingPDF : boolean = false;
  isProcessing : boolean = true;

  exportData: any[];

  actions : ActionDTO[] = [];



  constructor(
    public toastService: NbToastrService,
    private mediaService : MediaService,
    private pdfService : PdfService,
    private excelService : ExcelService,
    private authService : AuthService,
    private modal: NgbModal, 
    private usersService : UsersService,
    private router: Router,
    private route: ActivatedRoute

  ) {
   }

  ngOnInit(): void {

    
    this.actions = [ {action: 'edit', text: 'Modificar contacto'}, {action: 'delete', text: 'Eliminar contacto'}];
    this.getAddressBook(this.page);

  }


  roleName(therole) { return rolName(therole); }
  

  


  getAddressBook(page :number) 
  {
    this.isProcessing = true;
    console.log("isProcessing 0 =" + this.isProcessing);

    //if(!this.dayfrom) this.dayfrom = null;
    //if(!this.dayto) this.dayto = null;
    this.page = page
    this.usersService.getAddressBook(this.page, this.size, this.type, this.fullname, this.phone, this.email).subscribe(
      res => {
        this.isProcessing = false;
        console.log("isProcessing 1=" + this.isProcessing);
        this.contacts = res.content.map(item => { return {id: item.id, values: [item.fullname, item.phone, item.email, item.observations] }; });
        this.contactsObjects = res.content;

        this.totalPages = res.totalPages;
      },
      error => {
        this.isProcessing = false;
        console.error("getAddressBook():"+JSON.stringify(error));
        this.toastService.show("No se han podido obtener los contactos.",
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



    this.usersService.getAddressBook(0, 100000000, this.type, this.fullname, this.phone, this.email).subscribe(
      res => {
        const header = {};
        const keys = ['Nombre completo', 'Teléfono', 'Email', 'Observaciones'];
        const fields = ['fullname', 'phone', 'email', 'observations'];
        fields.forEach((key, i) => header[key] = keys[i]);
        this.exportData = res && res.content ? res.content.map(item => [item.fullname, item.phone, item.email, item.observations ]) : null;
        const final = parseDataExport(fields, this.exportData);
        const title = 'Agenda de contactos';

        if (format === 'excel') 
        {
          this.excelService.exportAsExcelFile(title, [header].concat(final), title, fields);
          this.loadingExcel = false;
          this.loadingPDF = false;
        } 
        else 
        {
          const minFields = ['30%','10%','25%', '35%'];
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
    this.getAddressBook(page);
  }


  setPage(event) {
    this.page = event;
    this.getAddressBook(this.page);
  }

  action(event) 
  {
    if (event && event[0] === 'edit') 
    {
      const selected = { ...this.contactsObjects.find(item => item.id === event[1])};
      this.openAddAddressBookModal(selected);
    } 
    else if (event && event[0] === 'delete') 
    {
      this.deleteAddressBook(event[1]);
    }
  }


  canModify() : boolean
  {
    return this.authService.isManager() || this.authService.isAdmin();
  }



  openAddAddressBookModal(selected? : AddressBookDTO)
  {
    console.warn(JSON.stringify(selected));
    if(!selected)
    {
      this.selectedContact.id =''; 
      this.selectedContact.email =''; 
      this.selectedContact.phone = ''; 
      this.selectedContact.fullname ='';
      this.selectedContact.type = 'OTHER'
      this.selectedContact.observations = '';
    }
    else
    {
      this.selectedContact = selected;
    }



    this.modal.open(this.modalContent, { size: 'lg' });
  }

  saveAddressBook()
  {

    if(this.selectedContact.fullname == '') 
    {
      this.toastService.show("Debes especificar el nombre del contacto.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }


    this.isProcessing = true;

    this.usersService.saveAddressBook(this.selectedContact.id, this.selectedContact.fullname, this.selectedContact.phone, this.selectedContact.email, this.selectedContact.observations).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Contacto grabado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getAddressBook(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("saveReceipt():"+JSON.stringify(error));
        this.toastService.show("No se ha podido añadir el contacto.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }


  deleteAddressBook(id : string)
  {
    this.isProcessing = true;

    this.usersService.deleteAddressBook(id).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Contacto eliminado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getAddressBook(this.page);
      },
      error => {
        this.isProcessing = false;
        console.error("deleteAddressBook():"+JSON.stringify(error));
        this.toastService.show("No se ha podido eliminar el contacto.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }



  

  

}
