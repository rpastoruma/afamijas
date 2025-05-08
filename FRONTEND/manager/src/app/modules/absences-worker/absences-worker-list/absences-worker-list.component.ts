import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { NbDialogService, NbGlobalPhysicalPosition, NbToastrConfig, NbToastrService, NbGlobalLogicalPosition } from '@nebular/theme';
import { DeleteConfirmComponent } from 'src/app/shared/components/delete-confirm/delete-confirm.component';
import { PatientDTO, RelativeAbsenceDTO, RouteDTO, RouteStopDTO, WorkerAbsenceDTO, parseDataExport } from 'src/app/shared/models/models';
//import { UpdateAcademicTitlesComponent } from '../update-academic-titles/update-academic-titles.component';
import { RelativeAbsencesService } from 'src/app/core/services/relative-absences.service';
import { UsersService } from 'src/app/core/services/users.service';
import { toArray } from 'rxjs';
import { flatpickrFactory } from '../../calendar/mycalendar.module'; 
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { RoutesService } from 'src/app/core/services/routes.service';

@Component({
  selector: 'app-absences-worker-list',
  templateUrl: './absences-worker-list.component.html',
  styleUrls: ['./absences-worker-list.component.scss']
})
export class AbsencesWorkerListComponent implements OnInit {

  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;

  pageR : number = 0;
  pageW : number = 0;
  size : number = 5;
  totalPagesR : number = 0;
  totalPagesW : number = 0;
  idpatient : string;
  from : Date;
  to : Date;

  allPatients : PatientDTO[] = [];
  
  relativeAbsences: any[]  = []; // any => formato del listado
  relativeAbsencesObjects: RelativeAbsenceDTO[]  = [];

  workerAbsences: any[]  = []; // any => formato del listado
  workerAbsencesObjects: WorkerAbsenceDTO[]  = [];


  route : RouteDTO = null;
  myroutestop_today : RouteStopDTO = null;

  theWorkerAbsence : WorkerAbsenceDTO = {
    id: '',
    idpatient: '',
    patient_fullname: '',
    idworker: '',
    worker_fullname: '',
    idroutestop: '',
    routestop_name: '',
    comment: '',
    when: undefined
  }

  loadingE : boolean = false;
  isProcessing : boolean = false;


  constructor(
    public toastService: NbToastrService,
    private relativeAbsencesService : RelativeAbsencesService,
        private routesService : RoutesService,
    private usersService : UsersService,
    private modal: NgbModal, 
    private dialogService : NbDialogService
  ) { }

  ngOnInit(): void {
    this.getPatients();
    flatpickrFactory();

  }


  
  getPatients()
  {

    this.isProcessing = true;
      this.usersService.getAllPatients().subscribe(
        res => {
          this.isProcessing = false;
          this.allPatients = res;
          if(this.allPatients && this.allPatients.length>0) 
          {
            this.idpatient = this.allPatients[0].id;
            this.theWorkerAbsence.idpatient = this.idpatient;
            this.getRoute(this.idpatient)
            this.getRelativeAbsences(this.pageR);
            this.getWorkerAbsences(this.pageW);
          }
        },
        error => 
        {
          this.isProcessing = false;
          console.error("getPatients():"+JSON.stringify(error));
          this.toastService.show("No se pueden obtener los usuarios asociados a este familiar.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
        }
      );
  }
  


  getRelativeAbsences(page: number) 
  {
    this.pageR = page;
    this.isProcessing = true;
    this.relativeAbsencesService.getRelativeAbsences(this.pageR, this.size, this.idpatient, this.from, this.to).subscribe(
      res => {
        this.isProcessing = false;
        this.relativeAbsences = res.content.map(item => { return {id: item.id, values: [this.date2Text(item.from), this.date2Text(item.to), item.transport, item.comment]  }; });
        this.relativeAbsencesObjects = res.content.map( item => this.convertDates(item) );
        this.totalPagesR = res.totalPages;
      },
      error => {
        this.isProcessing = false;
        console.error("getRelativeAbsences():"+JSON.stringify(error));
        this.toastService.show("No se han podido obtener las faltas por familiar.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }

  

  getWorkerAbsences(page: number) 
  {
    this.pageW = page;
    this.isProcessing = true;
    this.relativeAbsencesService.getWorkerAbsences(this.pageW, this.size, this.idpatient, this.from, this.to).subscribe(
      res => {
        this.isProcessing = false;
        this.workerAbsences = res.content.map(item => { return {id: item.id, values: [this.date2Text(item.when), item.worker_fullname, item.route_name, item.comment]  }; });
        this.workerAbsencesObjects = res.content.map( item => this.convertDates(item) );
        this.totalPagesW = res.totalPages;
      },
      error => {
        this.isProcessing = false;
        console.error("getWorkerAbsences():"+JSON.stringify(error));
        this.toastService.show("No se han podido obtener las faltas por familiar.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }

  

  goToCreate()
  {
    this.theWorkerAbsence.id = ''; //nuevo
    this.modal.open(this.modalContent, { size: 'lg' });
  }


  
  

  saveWorkerAbsence()
  {
    if(this.theWorkerAbsence.when==null)
    { 
      this.toastService.show("Debes completar la fecha de la falta.",
      "¡Ups!", 
      { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false }
      );
      return; 
    } 
    
    //const dfrom = new Date(this.theAbsence.from);
    //const dto = new Date(this.theAbsence.to);



    this.isProcessing = true;
    const isNew = this.theWorkerAbsence.id=='';


    this.relativeAbsencesService.saveWorkerAbsence(this.theWorkerAbsence.id, this.theWorkerAbsence.idpatient, this.theWorkerAbsence.idroutestop, this.theWorkerAbsence.comment, this.theWorkerAbsence.when).subscribe(
      res => {
        this.isProcessing = false;
        this.getWorkerAbsences(0);
        this.toastService.show(this.theWorkerAbsence.id=='Falta añadida correctamente'?"":"Falta actualizada correctamente.",
                               "¡Ok!", 
                               { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
                              );
        this.modal.dismissAll();
      },
      error => {
        console.error("saveWorkerAbsence():"+JSON.stringify(error));
        this.isProcessing = false;
        this.toastService.show("No se ha podido añadir/modificar la falta.",
                               "¡Ups!", 
                               { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
                              );
      }
    );


  }



  getRoute(idpatient : string)
  {
    this.routesService.getRouteByPatientForRelative(idpatient).subscribe(
      res => {
        if(res==null || res == '')
        {
          this.route.route_name = "Este usuario no tiene asignada ruta.";
          this.myroutestop_today = null;
        }
        else
        {
          this.route = res;
        }

          
        for(let routestop of this.route.routestops)
        {
          if(routestop.idroutestop == this.route.idroutestop_selected_today)
            this.myroutestop_today = {...routestop};
        }
      },
      error => {
        console.error("getRoute():"+JSON.stringify(error));
        this.toastService.show("No se puede obtener la ruta.",
                               "¡Ups!", 
                               { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
                              );
      }
    );
      
  }

  setPageR(event) 
  {
    this.pageR = event;
    this.getRelativeAbsences(this.pageR);
  }

  


  setPageW(event) 
  {
    this.pageW = event;
    this.getWorkerAbsences(this.pageW);
  }




  filterR(page: number) {
    this.getRelativeAbsences(page);
  }

  

  filterW(page: number) {
    this.getWorkerAbsences(page);
  }


  action(event) 
  {

    if (event && event[0] === 'edit') 
    {
      const selected = this.workerAbsencesObjects.find(item => item.id === event[1]);
      this.theWorkerAbsence = selected;
      this.modal.open(this.modalContent, { size: 'lg' });
    } 
    else if (event && event[0] === 'delete') 
    {
      this.deleteWorkerAbsence(event[1], event[2]);
    }
  }

  deleteWorkerAbsence(id, selected) {
    console.log("SELECTED=" + JSON.stringify(selected));
    const dialogRef = this.dialogService.open(DeleteConfirmComponent, {
      hasScroll: true,
      closeOnBackdropClick: false,
      closeOnEsc: true,
      context: {
        text: 'Por favor, confirma que quieres eliminar la falta ',
        value: this.value2Text(selected.values)
      }
    });
    dialogRef.onClose.subscribe(
      res => {
        if (res === 'confirm') {
          this.relativeAbsencesService.deleteWorkerAbsence(id).subscribe(
            result => {
              this.getWorkerAbsences(this.pageW);
              this.toastService.show("Falta eliminada correctamente.",
              "¡Ok!", 
              { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
             );
            },
            error => {
              console.error("deleteWorkerAbsence():"+JSON.stringify(error));
              this.isProcessing = false;
              this.toastService.show("No se ha podido eliminar la falta.",
                               "¡Ups!", 
                               { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
                              );
            }
          );
        }
      }
    );
  }


  disabledDay(date) {
    date.setDate(date.getDate() + 1);
   return date < new Date();
  }



  //[2024,3,1,13,0,...] --> Date
  localDateTime2Date(thedate : number[]) : Date
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
    return new Date(Date.parse(sdate));
  }

  
  date2Text(thelocaldatetime : number[])
  {
    let thedate : Date = this.localDateTime2Date(thelocaldatetime);
    if(!thedate) return '';
    let daysOfWeek = [ 'domingo', 'lunes', 'martes', 'miércoles', 'jueves', 'viernes', 'sábado' ];
    const dayOfWeek = daysOfWeek[thedate.getDay()];
    return 'El ' + daysOfWeek[thedate.getDay()] + " " + this.completeZeros(thedate.getDate()) + "/" + this.completeZeros(thedate.getMonth()+1) + "/" + (thedate.getFullYear()+"") + " a las " +  this.completeZeros(thedate.getHours()) + ":" + this.completeZeros(thedate.getMinutes()) + " h.";
  }


  completeZeros(x : number) : string
  {
    if(x<=9) return "0" + x;
    else return ""+x;
  }



  convertDates(absence: any)
  {
    if(absence.from) absence.from = this.localDateTime2Date(absence.from);
    if(absence.to) absence.to = this.localDateTime2Date(absence.to);
    if(absence.when) absence.when = this.localDateTime2Date(absence.when);
    return absence
  }


  value2Text(thevalue : any[]) : string
  {
    let res = '';
    if(thevalue[0]) res += "de " + thevalue[0].toLowerCase();
    return res;
  }
}
