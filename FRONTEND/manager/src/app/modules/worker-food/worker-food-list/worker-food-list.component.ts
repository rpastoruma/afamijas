import { Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import { PatientDTO, MedicationDTO, parseDataExport, ActionDTO, MenuDTO, FoodDTO } from 'src/app/shared/models/models';
import { UsersService } from 'src/app/core/services/users.service';
import { PdfService } from 'src/app/core/services/pdf-service.service';
import { ExcelService } from 'src/app/core/services/excel-service.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-worker-food-list',
  templateUrl: './worker-food-list.component.html',
  styleUrls: ['./worker-food-list.component.scss']
})
export class WorkerFoodListComponent implements OnInit{

  

  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;


  page : number = 0;
  size : number = 5;
  totalPages : number = 0;
  idpatient : string = '';

  allPatients : PatientDTO[] = [];
  allMenuTypes : string[] = ['BASAL', 'DIABÉTICO', 'TRITURADO', 'HIPOSÓDICO', 'ALÉRGICO']

  theFood : FoodDTO = {
    idpatient: '',
    patient_fullname: '',
    menu_type: '',
    breakfast_description: ''
  };
  
  foods: any[]  = []; // amy => formato del listado
  foodsObjects: FoodDTO[]  = [];

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
    if(this.canModify()) this.actions = [{action: 'edit', text: 'Modificar menús/desaynos'}];
  }

  getPatients()
  {

      this.usersService.getAllPatients().subscribe(
        res => {
          this.allPatients = res;
          if(this.allPatients && this.allPatients.length>0) 
          {
            this.getFoods(0);
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
  


  getFoods(page :number) 
  {
    this.page = page
    this.usersService.getFoods(this.idpatient, this.page, this.size).subscribe(
      res => {
        this.isProcessing = false;
        this.foods = res.content.map(item => { return {id: item.idpatient, values: [item.patient_fullname, item.breakfast_description, item.menu_type]  }; });
        this.foodsObjects = res.content;

        this.totalPages = res.totalPages;
      },
      error => {
        this.isProcessing = false;
        console.error("getFood():"+JSON.stringify(error));
        this.toastService.show("No se ha podido obtener la información de los menús/desayunos.",
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

    this.usersService.getFoods(this.idpatient, 0, 100000000).subscribe(
      res => {
        const header = {};
        const keys = ['Usuario', 'Desayuno', 'Tipo de menú'];
        const fields = ['patient_fullname', 'breakfast_description', 'menu_type'];
        fields.forEach((key, i) => header[key] = keys[i]);
        this.exportData = res && res.content ? res.content.map(item => [item.patient_fullname, item.breakfast_description, item.menu_type]) : null;
        const final = parseDataExport(fields, this.exportData);
        const title = 'Listado de comidas';

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
    this.getFoods(page);
  }


  setPage(event) {
    this.page = event;
    this.getFoods(this.page);
  }

  action(event) 
  {
    if (event && event[0] === 'edit') 
    {
      console.log("event=" + JSON.stringify(event));
      console.log("selected=" + JSON.stringify(this.foodsObjects));

      const selected = this.foodsObjects.find(item => item.idpatient === event[1]);
      this.theFood = selected;

      console.log("selected=" + JSON.stringify(selected));

      this.openMofifyFoodModal();
    } 
    else if (event && event[0] === 'show') 
    {
      console.log("event=" + JSON.stringify(event));
      console.log("selected=" + JSON.stringify(this.foodsObjects));

      const selected = this.foodsObjects.find(item => item.idpatient === event[1]);
      this.theFood = selected;

      console.log("selected=" + JSON.stringify(selected));

      this.openMofifyFoodModal();
    } 
  }


  canModify() : boolean
  {
    return this.authService.isAdmin() || this.authService.isManager();
  }



  openMofifyFoodModal()
  {

    this.modal.open(this.modalContent, { size: 'lg' });
  }

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
  }


}
