import { Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import { PatientDTO, MedicationDTO, parseDataExport, ActionDTO } from 'src/app/shared/models/models';
import { UsersService } from 'src/app/core/services/users.service';
import { PdfService } from 'src/app/core/services/pdf-service.service';
import { ExcelService } from 'src/app/core/services/excel-service.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-worker-medication-list',
  templateUrl: './worker-medication-list.component.html',
  styleUrls: ['./worker-medication-list.component.scss']
})
export class WorkerMedicationListComponent implements OnInit {

  

  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;


  page : number = 0;
  size : number = 5;
  totalPages : number = 0;
  idpatient : string = '';

  allPatients : PatientDTO[] = [];

  theMedication : MedicationDTO = {
    idpatient: '',
    patient_fullname: '',
    medication_description_morning: '',
    medication_description_evening: ''
  };
  
  medications: any[]  = []; // any => formato del listado
  medicationsObjects: MedicationDTO[]  = [];

  loadingExcel : boolean = false;
  loadingPDF : boolean = false;
  isProcessing : boolean = true;

  exportData: any[];

  actions : ActionDTO[] = [];

  constructor(
    public toastService: NbToastrService,
    private usersService : UsersService,
    private pdfService : PdfService,
    private excelService : ExcelService,
    private authService : AuthService,
    private modal: NgbModal, 

  ) { }

  ngOnInit(): void {
    this.getPatients();
    if(this.canModify()) this.actions = [{action: 'edit', text: 'Modificar medicación'}];
  }

  getPatients()
  {

      this.usersService.getAllPatients().subscribe(
        res => {
          this.allPatients = res;
          if(this.allPatients && this.allPatients.length>0) 
          {
            this.getMedications(0);
          }
        },
        error => 
        {
          console.error("getPatients():"+JSON.stringify(error));
          this.toastService.show("No se pueden obtener los usuarios asociados a este familiar.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
        }
      );
  }
  


  getMedications(page :number) 
  {
    this.page = page
    this.usersService.getMedications(this.idpatient, this.page, this.size).subscribe(
      res => {
        this.isProcessing = false;
        this.medications = res.content.map(item => { return {id: item.idpatient, values: [item.patient_fullname, item.medication_description_morning, item.medication_description_evening]  }; });
        this.medicationsObjects = res.content;

        this.totalPages = res.totalPages;
      },
      error => {
        this.isProcessing = false;
        console.error("getMedications():"+JSON.stringify(error));
        this.toastService.show("No se ha podido obtener la información de la medicación.",
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

    this.usersService.getMedications(this.idpatient, 0, 100000000).subscribe(
      res => {
        const header = {};
        const keys = ['Usuario', 'Medicación mañana', 'Medicación tarde'];
        const fields = ['patient_fullname', 'description', 'typename'];
        fields.forEach((key, i) => header[key] = keys[i]);
        this.exportData = res && res.content ? res.content.map(item => [item.patient_fullname, item.medication_description_morning, item.medication_description_evening]) : null;
        const final = parseDataExport(fields, this.exportData);
        const title = 'Listado de medicación';

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
    this.getMedications(page);
  }


  setPage(event) {
    this.page = event;
    this.getMedications(this.page);
  }

  action(event) 
  {
    if (event && event[0] === 'edit') 
    {
      console.log("event=" + JSON.stringify(event));
      console.log("selected=" + JSON.stringify(this.medicationsObjects));

      const selected = this.medicationsObjects.find(item => item.idpatient === event[1]);
      this.theMedication = selected;

      console.log("selected=" + JSON.stringify(selected));

      this.openMofifyMedicationModal();
    } 
    else if (event && event[0] === 'show') 
    {
      console.log("event=" + JSON.stringify(event));
      console.log("selected=" + JSON.stringify(this.medicationsObjects));

      const selected = this.medicationsObjects.find(item => item.idpatient === event[1]);
      this.theMedication = selected;

      console.log("selected=" + JSON.stringify(selected));

      this.openMofifyMedicationModal();
    } 
  }


  canModify() : boolean
  {
    return this.authService.isAdmin() || this.authService.isManager();
  }



  openMofifyMedicationModal()
  {

    this.modal.open(this.modalContent, { size: 'lg' });
  }

  modifyMedication()
  {
    this.usersService.modifyMedication(this.theMedication.idpatient, this.theMedication.medication_description_morning, this.theMedication.medication_description_evening).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Medicación modificada correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getMedications(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("modifyMedication():"+JSON.stringify(error));
        this.toastService.show("No se ha podido modificar la medicación correctamente.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }


}
