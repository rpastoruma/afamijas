import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { NbDialogService, NbGlobalPhysicalPosition, NbToastrConfig, NbToastrService, NbGlobalLogicalPosition } from '@nebular/theme';
import { DeleteConfirmComponent } from 'src/app/shared/components/delete-confirm/delete-confirm.component';
import { PatientDTO, RelativeAbsenceDTO, parseDataExport } from 'src/app/shared/models/models';
//import { UpdateAcademicTitlesComponent } from '../update-academic-titles/update-academic-titles.component';
import { RelativeAbsencesService } from 'src/app/core/services/relative-absences.service';
import { UsersService } from 'src/app/core/services/users.service';
import { toArray } from 'rxjs';
import { flatpickrFactory } from '../../calendar/mycalendar.module'; 
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-absences-list',
  templateUrl: './absences-list.component.html',
  styleUrls: ['./absences-list.component.scss']
})
export class AbsencesListComponent implements OnInit {

  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;

  page : number = 0;
  size : number = 5;
  totalPages : number = 0;
  idpatient : string;
  from : Date;
  to : Date;

  allPatients : PatientDTO[] = [];
  
  relativeAbsences: any[]  = []; // any => formato del listado
  relativeAbsencesObjects: RelativeAbsenceDTO[]  = [];

  theAbsence : RelativeAbsenceDTO = {
    id: '',
    idpatient: '',
    idrelative: '',
    patient_fullname: '',
    transport: 'NO',
    comment: '',
    allday: false,
    from: null,
    to: null
  }

  
  loadingE : boolean = false;
  isProcessing : boolean = true;


  constructor(
    public toastService: NbToastrService,
    private relativeAbsencesService : RelativeAbsencesService,
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

      this.usersService.getPatientsByRelative().subscribe(
        res => {
          this.allPatients = res;
          if(this.allPatients && this.allPatients.length>0) 
          {
            this.idpatient = this.allPatients[0].id;
            this.theAbsence.idpatient = this.idpatient;
            this.getRelativeAbsences(this.page);
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
  


  getRelativeAbsences(page: number) 
  {
    this.page = page;
    this.isProcessing = true;
    this.relativeAbsencesService.getRelativeAbsences(this.page, this.size, this.idpatient, this.from, this.to).subscribe(
      res => {
        this.isProcessing = false;
        this.relativeAbsences = res.content.map(item => { return {id: item.id, values: [this.date2Text(item.from), this.date2Text(item.to), item.transport, item.comment]  }; });
        this.relativeAbsencesObjects = res.content.map( item => this.convertDates(item) );
        this.totalPages = res.totalPages;
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

  

  goToCreate()
  {
    this.theAbsence.id = ''; //nuevo
    this.modal.open(this.modalContent, { size: 'lg' });
  }


  
  

  saveRelativeAbsence()
  {
    if(this.theAbsence.from==null || this.theAbsence.to==null)
    { 
      this.toastService.show("Debes completar las fechas/horas de inicio y fin de la falta.",
      "¡Ups!", 
      { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false }
      );
      return; 
    } 
    
    //const dfrom = new Date(this.theAbsence.from);
    //const dto = new Date(this.theAbsence.to);


    if(this.theAbsence.to < this.theAbsence.from) 
    { 
      this.toastService.show("El momento de inicio de la falta no puede ser posterior a la de su fin.",
      "¡Ups!", 
      { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return; 
    } 

    this.isProcessing = true;
    const isNew = this.theAbsence.id=='';


    this.relativeAbsencesService.saveAbsenceByRelative(this.theAbsence.id, this.theAbsence.idpatient, this.theAbsence.from, this.theAbsence.to, this.theAbsence.allday, this.theAbsence.transport, this.theAbsence.comment).subscribe(
      res => {
        this.isProcessing = false;
        this.getRelativeAbsences(0);
        this.toastService.show(this.theAbsence.id=='Falta añadida correctamente'?"":"Falta actualizada correctamente.",
                               "¡Ok!", 
                               { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
                              );
        if(isNew) this.modal.dismissAll();
      },
      error => {
        console.error("saveAbsenceByRelative():"+JSON.stringify(error));
        this.isProcessing = false;
        this.toastService.show("No se ha podido añadir/modificar la falta.",
                               "¡Ups!", 
                               { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
                              );
      }
    );


  }



  setPage(event) 
  {
    this.page = event;
    this.getRelativeAbsences(this.page);
  }



  filter(page: number) {
    this.getRelativeAbsences(page);
  }


  action(event) 
  {

    if (event && event[0] === 'edit') 
    {
      const selected = this.relativeAbsencesObjects.find(item => item.id === event[1]);
      this.theAbsence = selected;
      this.modal.open(this.modalContent, { size: 'lg' });
    } 
    else if (event && event[0] === 'delete') 
    {
      this.deleteAbsence(event[1], event[2]);
    }
  }

  deleteAbsence(id, selected) {
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
          this.relativeAbsencesService.deleteAbsence(id, this.idpatient).subscribe(
            result => {
              this.getRelativeAbsences(this.page);
              this.toastService.show("Falta eliminada correctamente.",
              "¡Ok!", 
              { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
             );
            },
            error => {
              console.error("saveAbsenceByRelative():"+JSON.stringify(error));
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
    absence.from = this.localDateTime2Date(absence.from);
    absence.to = this.localDateTime2Date(absence.to);
    return absence
  }


  value2Text(thevalue : any[]) : string
  {
    let res = '';
    if(thevalue[0]) res += "desde " + thevalue[0].toLowerCase();
    if(thevalue[1]) res += " hasta " + thevalue[1].toLowerCase();
    if(thevalue[2]) res += " - TRANSPORTE: " + thevalue[2];
    return res;
  }
}
