

import { Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import { parseDataExport, ActionDTO, DocDTO, ProjectDTO } from 'src/app/shared/models/models';
import { PdfService } from 'src/app/core/services/pdf-service.service';
import { ExcelService } from 'src/app/core/services/excel-service.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { flatpickrFactory } from '../../calendar/mycalendar.module'; 
import { DocsService } from 'src/app/core/services/docs.service';
import { Subscription, finalize } from 'rxjs';
import { MediaService } from 'src/app/core/services/media.service';
import { HttpEventType } from '@angular/common/http';

@Component({
  selector: 'app-projects-list',
  templateUrl: './projects-list.component.html',
  styleUrls: ['./projects-list.component.scss']
})
export class ProjectsListComponent implements OnInit {

  

  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;
  @ViewChild('modalContent2', { static: true }) modalContent2: TemplateRef<any>;

  modalRef2: any;

  //PARÁMETROS LISTADO
  dayfrom : Date = null; 
  dayto : Date = null;
  theText : string = '';
  theSubvencion : string = '';

  page : number = 0;
  size : number = 4;
  totalPages : number = 0;

  requiredFileType:string  = ".png,.jpg,.jpeg,.webp,.gif,.pdf,.doc,.docx,.xsl,.xslx";
  fileName = '';
  uploadProgress:number;
  uploadSub: Subscription;

  theDoc : DocDTO = {
    id: '',
    idworker: '',
    worker_fullname: '',
    title: '',
    description: '',
    url: '',
    dayfrom: null,
    dayto: null,
    roles: [],
    created: undefined
  };

  theProject : ProjectDTO = {
    id: '',
    idworker: '',
    worker_fullname: '',
    nombre: '',
    fecha_presentacion: undefined,
    fecha_resolucion: undefined,
    plazo_ejecucion: '',
    subvencion_concedida: false,
    importe_solicitado: 0,
    importe_concedido: 0,
    documentos: []
  }

  thedocsvalues: any[] = [];
  
  projects: any[]  = []; // amy => formato del listado
  projectsObjects: ProjectDTO[]  = [];

  loadingExcel : boolean = false;
  loadingPDF : boolean = false;
  isProcessing : boolean = true;

  exportData: any[];

  actions : ActionDTO[] = [];
  actions2 : ActionDTO[] = [];
  createEvent: boolean = false;

  constructor(
    public toastService: NbToastrService,
    private mediaService : MediaService,
    private pdfService : PdfService,
    private excelService : ExcelService,
    private authService : AuthService,
    private modal: NgbModal, 
    private docsService : DocsService

  ) {
   }

  ngOnInit(): void {
    flatpickrFactory();
    this.dayfrom = null;
    this.dayto = null;
    this.actions = [{action: 'edit', text: 'Modificar proyecto'}, {action: 'delete', text: 'Eliminar proyecto'}];
    this.actions2 = [{action: 'show', text: 'Ver documento'}, {action: 'delete', text: 'Eliminar documento'}];
    this.getProjects(0);
  }





  getProjects(page :number) 
  {
    this.isProcessing = true;
    //if(!this.dayfrom) this.dayfrom = null;
    //if(!this.dayto) this.dayto = null;
    this.page = page
    this.docsService.getProjects(this.page, this.size, this.dayfrom, this.dayto, this.theText, this.theSubvencion).subscribe(
      res => {
        this.isProcessing = false;
        this.projects = res.content.map(item => { return {id: item.id, values: [item.nombre, this.date2Text1(item.fecha_presentacion), this.date2Text1(item.fecha_resolucion), item.plazo_ejecucion, item.subvencion_concedida, item.importe_solicitado, item.importe_concedido ] }; });
        this.projectsObjects = res.content;

        this.totalPages = res.totalPages;

        

        const updatedProject = this.projectsObjects.find(p => p.id === this.theProject.id);
        if (updatedProject) {
          this.thedocsvalues = updatedProject.documentos.map(item => { return {id: item.id, values: [item.title, item.description, this.date2Text(item.dayfrom)  ] }; });
        } 


      },
      error => {
        this.isProcessing = false;
        console.error("getProjects():"+JSON.stringify(error));
        this.toastService.show("No se han podido obtener los proyectos.",
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

    //if(!this.dayfrom) this.dayfrom = new Date();
    //if(!this.dayto) this.dayto = new Date();

    this.docsService.getProjects(0, 100000000, this.dayfrom, this.dayto, this.theText, this.theSubvencion).subscribe(
      res => {
        const header = {};
        const keys = ['Nombre:', 'Presentación:',  'Resolución:', 'Plazo:', 'Subvención:', 'Solicitado:', 'Concedido:'];
        const fields = ['nombre', 'fecha_presentacion', 'fecha_resolucion', 'plazo_ejecucion', 'subvencion_concedida', 'subvencion_concedida', 'importe_solicitado', 'importe_concedido'];
        fields.forEach((key, i) => header[key] = keys[i]);
        this.exportData = res && res.content ? res.content.map(item => [item.nombre, this.date2Text1(item.fecha_presentacion), this.date2Text1(item.fecha_resolucion), item.plazo_ejecucion, item.subvencion_concedida, item.importe_solicitado, item.importe_concedido ]) : null;
        const final = parseDataExport(fields, this.exportData);
        const title = 'Proyectos';

        if (format === 'excel') 
        {
          this.excelService.exportAsExcelFile(title, [header].concat(final), title, fields);
          this.loadingExcel = false;
          this.loadingPDF = false;
        } 
        else 
        {
          const minFields = ['30%','10%','10%','20%','10%','10%','10%'];
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
    this.getProjects(page);
  }


  setPage(event) {
    this.page = event;
    this.getProjects(this.page);
  }

  action(event) 
  {
    if (event && event[0] === 'edit') 
    {
      const selected = { ...this.projectsObjects.find(item => item.id === event[1])};
      this.openAddProjectModal(selected);
    } 
    else if (event && event[0] === 'delete') 
    {
      this.deleteProject(event[1]);
    }

  }


  action2(event) 
  {
    if (event && event[0] === 'delete') 
    {
      this.deleteDocProject(event[1]);
    }
    else if (event && event[0] === 'show') 
    {
      const selected =  {...this.theProject.documentos.find(item => item.id === event[1])};
      window.open(selected.url);
    } 

  }

  deleteDocProject(id : string)
  {
    this.docsService.deleteDocProject(id, this.theProject.id).subscribe(
      res => {
        this.isProcessing = false;

        // Eliminar el documento localmente de theProject.documentos
      this.theProject.documentos = this.theProject.documentos.filter(doc => doc.id !== id);
      console.warn("deleteDocProject():"+JSON.stringify(this.theProject.documentos));


        this.toastService.show("Documento eliminado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getProjects(this.page);
      },
      error => {
        this.isProcessing = false;
        console.error("deleteDocProject():"+JSON.stringify(error));
        this.toastService.show("No se ha podido eliminar el documento.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }

  
  filter2(page: number) {
    
  }


  openAddProjectModal(selected? : ProjectDTO)
  {
    console.warn(JSON.stringify(selected));
    if(!selected)
    {
      this.theProject.id =''; 
      this.theProject.nombre =''; 
      //this.theProject.fecha_presentacion = new Date();
      //this.theProject.fecha_resolucion = new Date();
      this.theProject.plazo_ejecucion = '';
      this.theProject.subvencion_concedida = false;
      this.theProject.importe_solicitado = 0;
      this.theProject.importe_concedido = 0;
    }
    else
    {
      this.theProject = selected;
      this.theProject.fecha_presentacion = this.localDateTime2Date(this.theProject.fecha_presentacion);
      this.theProject.fecha_resolucion = this.localDateTime2Date(this.theProject.fecha_resolucion);
    }

    this.thedocsvalues = this.theProject.documentos.map(item => { return {id: item.id, values: [item.title, item.description, this.date2Text(item.dayfrom)  ] }; });



    this.modal.open(this.modalContent, { size: 'lg' });
  }

  

  openAddDocumentModal()
  {
    this.fileName = undefined;
    this.theDoc.id =''; 
    this.theDoc.url =''; 
    this.theDoc.title =''; 
    this.theDoc.description ='';
    //this.theDoc.dayfrom = new Date();
    this.theDoc.dayto = null;
    this.theDoc.roles = null;


    this.modalRef2 = this.modal.open(this.modalContent2, { size: 'lg' });
  }

  saveDocProject()
  {
    if(this.theDoc.title == '') 
    {
      this.toastService.show("Debes indicar un título para el documento.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }

    if(this.theDoc.url == '') 
    {
      this.toastService.show("Debes indicar un fichero para el documento.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }

    this.docsService.saveDocProject(this.theDoc, this.theProject.id).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Documento grabado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );

          this.getProjects(this.page);
          
          this.modalRef2.close();
        
      },
      error => {
        this.isProcessing = false;
        console.error("saveDocProject():"+JSON.stringify(error));
        this.toastService.show("No se ha podido añadir el documento.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }


  saveProject()
  {


    if(this.theProject.nombre == '') 
    {
      this.toastService.show("Debes indicar un nombre para el proyecto.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }
   

    this.docsService.saveProject(this.theProject).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Proyecto grabado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getProjects(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("saveProject():"+JSON.stringify(error));
        this.toastService.show("No se ha podido añadir el proyecto.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }


  deleteProject(id : string)
  {
    this.docsService.deleteProject(id).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Proyecto eliminado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getProjects(this.page);
      },
      error => {
        this.isProcessing = false;
        console.error("deleteFeeding():"+JSON.stringify(error));
        this.toastService.show("No se ha podido eliminar el proyecto.",
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
    const upload$ = this.mediaService.uploadFile("document", file).pipe(finalize(() => this.reset())
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
            this.theDoc.url = event.url;
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
            this.theDoc.url = event.url;
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
    console.warn("date2Text2():"+JSON.stringify(thedate));
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

  

/*

  paginateDocs<T>(docs: T[], pageNumber: number, pageSize: number): PaginationResult<T> {
    const totalElements = docs.length;
    const totalPages = Math.ceil(totalElements / pageSize);

    const start = pageNumber * pageSize;
    const end = start + pageSize;
    const pageContent = docs.slice(start, end);

    const empty = pageContent.length === 0;

    return {
        content: pageContent,
        pageable: {
            pageNumber,
            pageSize,
            sort: {
                empty: true,
                sorted: false,
                unsorted: true,
            },
            offset: start,
            paged: true,
            unpaged: false,
        },
        last: pageNumber >= totalPages - 1,
        totalPages,
        totalElements,
        size: pageSize,
        number: pageNumber,
        sort: {
            empty: true,
            sorted: false,
            unsorted: true,
        },
        first: pageNumber === 0,
        numberOfElements: pageContent.length,
        empty,
    };
}*/

}
