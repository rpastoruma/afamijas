import { Component,  ChangeDetectionStrategy,  ViewChild,  TemplateRef, ViewEncapsulation, OnInit } from '@angular/core';
import { startOfDay,  endOfDay,  subDays,  addDays,  endOfMonth,  isSameDay,  isSameMonth,  addHours,} from 'date-fns';
import { Subject } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {  CalendarEventTimesChangedEvent,  CalendarView, CalendarDateFormatter,  DAYS_OF_WEEK, CalendarMonthViewBeforeRenderEvent, CalendarWeekViewBeforeRenderEvent, CalendarDayViewBeforeRenderEvent, CalendarEvent} from 'angular-calendar';
import { EventColor } from 'calendar-utils';
import { registerLocaleData } from '@angular/common';
import { AuthService } from 'src/app/core/services/auth.service';
import localeEs from '@angular/common/locales/es';
import { UsersService } from 'src/app/core/services/users.service';
import { NbDialogService, NbGlobalPhysicalPosition } from '@nebular/theme';
import { DeleteConfirmComponent } from 'src/app/shared/components/delete-confirm/delete-confirm.component';
import { hasRole, RoleCode, rolName, RouteDTO, RouteStopDTO, PatientDTO } from 'src/app/shared/models/models';
import { flatpickrFactory } from '../../calendar/mycalendar.module'; 
import { RoutesService } from 'src/app/core/services/routes.service';
import { NbToastrService } from '@nebular/theme';

@Component({
  selector: 'app-relative-route',
  templateUrl: './relative-route.component.html',
  styleUrls: ['./relative-route.component.scss']
})
export class RelativeRouteComponent implements OnInit {

  constructor(
    private modal: NgbModal, 
    private usersService : UsersService,
    private authService : AuthService,
    private dialogService: NbDialogService,
    private routesService : RoutesService,
    private toastService : NbToastrService
  ) 
  {
    registerLocaleData(localeEs);
  }


  ngOnInit(): void 
  {
    this.getPatients();
    flatpickrFactory();
  }


  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;

  idpatient : string = '';
  allPatients : PatientDTO[] = [];
  route : RouteDTO = {
    idroute: '',
    route_name: '',
    idpatient: '',
    patient_fullname: '',
    routestops: [],
    idroutestop_selected_today: '',
    idroutestop_selected_tomorrow: '',
    routestop_especial_from: '',
    routestop_especial_to: ''
  };
  
  myroutestop_today : RouteStopDTO = {
    idroutestop: '',
    order: 0,
    name: '',
    hour: '',
    postaladdress: [],
    idcity: '',
    idstate: '',
    postalcode: '',
    lat: 0,
    lon: 0
  }

  myroutestop_tomorrow : RouteStopDTO = {
    idroutestop: '',
    order: 0,
    name: '',
    hour: '',
    postaladdress: [],
    idcity: '',
    idstate: '',
    postalcode: '',
    lat: 0,
    lon: 0
  }

  cambioPuntual : boolean = false;
  from : string = '';
  to : string = '';
  newRouteStop : string = '';
  processing : boolean = false;
  
  getPatients()
  {

      this.usersService.getPatientsByRelative().subscribe(
        res => {
          this.allPatients = res;
          if(this.allPatients && this.allPatients.length>0) 
          {
            this.idpatient = this.allPatients[0].id;
            this.getRoute(this.idpatient);
          }
          //this.events = this.events.concat(this.test_events);
          //console.log("this.users=>" +  JSON.stringify(this.allPatients));
        },
        error => 
        {
          console.error("getPatients():"+JSON.stringify(error));
          this.toastService.show("No se pueden obtener los usuarios asociados a este familiar.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 5000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
        }
      );
  }
  

  getRoute(idpatient : string)
  {
    this.routesService.getRouteByPatientForRelative(idpatient).subscribe(
      res => {
        if(res==null || res == '')
          this.route.route_name = "Este usuario no tiene asignada ruta.";
        else
        {
          this.route = res;
          this.newRouteStop = (this.route.routestops && this.route.routestops.length>0)?this.route.routestops[0].idroutestop:'';
        }

          
        for(let routestop of this.route.routestops)
        {
          if(routestop.idroutestop == this.route.idroutestop_selected_today)
            this.myroutestop_today = {...routestop};
          
          if(routestop.idroutestop == this.route.idroutestop_selected_tomorrow)
            this.myroutestop_tomorrow = {...routestop};
        }
      },
      error => {
        console.error("getRoute():"+JSON.stringify(error));
        this.toastService.show("No se puede obtener la ruta.",
                               "¡Ups!", 
                               { status: 'danger', destroyByClick: true, duration: 5000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
                              );
      }
    );
      
  }
  

  openChangeRouteStopModal()
  {

    this.modal.open(this.modalContent, { size: 'lg' });
  }


  changeRouteStop(idpatient: string, idroutestop: string, from : string, to :string, cambioPuntual : boolean)
  {
    if(!cambioPuntual) { from = to = null; }

    if(from!=null && to==null) to = from;
    
    if(to < from) 
    { 
      this.toastService.show("La fecha de inicio del cambio no puede ser posterior a la de su fin.",
      "¡Ups!", 
      { status: 'danger', destroyByClick: true, duration: 5000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return; 
    } 

    this.processing = true;

    let sfrom = null; if (from!=null) sfrom = this.formatDate2(from);
    let sto = null; if (to!=null) sto = this.formatDate2(to);

    this.routesService.changeRouteStop(idpatient, idroutestop, sfrom , sto).subscribe(
      res => {
        this.processing = false;
        this.getRoute(idpatient);
        this.toastService.show("Ruta cambiada correctamente.",
                               "¡Ok!", 
                               { status: 'success', destroyByClick: true, duration: 5000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
                              );

      },
      error => {
        console.error("changeRouteStop():"+JSON.stringify(error));
        this.processing = false;
        this.toastService.show("Ha habido un error al cambiar la ruta.",
                               "¡Ups!", 
                               { status: 'danger', destroyByClick: true, duration: 5000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
                              );
      }
    );


  }

  
  formatDate(sdate : string)
  {
    let thedate = new Date(sdate);
    return this.completeZeros(thedate.getDate()) + "/" + this.completeZeros(thedate.getMonth()+1) + "/" + (thedate.getFullYear()+"");
  }

  
  formatDate2(sdate : string)
  {
    let thedate = new Date(sdate);
    return (thedate.getFullYear()+"")  + "-" + this.completeZeros(thedate.getMonth()+1) + "-" + this.completeZeros(thedate.getDate());
  }

  

  completeZeros(x : number) : string
  {
    if(x<=9) return "0" + x;
    else return ""+x;
  }

  disabledDay(date) {
    date.setDate(date.getDate() + 1);
   return date < new Date();
  }

}
