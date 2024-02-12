import { Component,  ChangeDetectionStrategy,  ViewChild,  TemplateRef, ViewEncapsulation, OnInit } from '@angular/core';
import { startOfDay,  endOfDay,  subDays,  addDays,  endOfMonth,  isSameDay,  isSameMonth,  addHours,} from 'date-fns';
import { Subject } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {  CalendarEventTimesChangedEvent,  CalendarView, CalendarDateFormatter,  DAYS_OF_WEEK, CalendarMonthViewBeforeRenderEvent, CalendarWeekViewBeforeRenderEvent, CalendarDayViewBeforeRenderEvent, CalendarEvent} from 'angular-calendar';
import { EventColor } from 'calendar-utils';
import { registerLocaleData } from '@angular/common';
import { CustomDateFormatter } from './custom-date-formatter.provider';
import { CalendarService } from 'src/app/core/services/calendar.service';
import { hasRole, RoleCode, rolName, UserDTO } from 'src/app/shared/models/models';
import { AuthService } from 'src/app/core/services/auth.service';
import { flatpickrFactory } from '../mycalendar.module';
import localeEs from '@angular/common/locales/es';
import { UsersService } from 'src/app/core/services/users.service';
import { NbDialogService, NbGlobalPhysicalPosition } from '@nebular/theme';
import { DeleteConfirmComponent } from 'src/app/shared/components/delete-confirm/delete-confirm.component';

import { NbToastrService } from '@nebular/theme';

@Component({
  selector: 'app-calendar-view',
  //changeDetection: ChangeDetectionStrategy.OnPush,  // si se activa hay que pulsar para que se detecten los cambios
  templateUrl: './calendar-view.component.html',
  encapsulation: ViewEncapsulation.None,
  styleUrls: ['./calendar-view.component.scss'],
  providers: [
    {
      provide: CalendarDateFormatter,
      useClass: CustomDateFormatter,
    },
  ],
})
export class CalendarViewComponent implements OnInit {

  constructor(
    private modal: NgbModal, 
    private calendarService : CalendarService,
    private usersService : UsersService,
    private authService : AuthService,
    private dialogService: NbDialogService,
    private toastService: NbToastrService
  ) 
  {
    registerLocaleData(localeEs);
  }


  ngOnInit(): void 
  {
    this.getAllUsers([""]);
    this.getCalendarEvents();
    flatpickrFactory();
  }


  processing : boolean = false;

  thestart : string;

  theend : string;

  thetitle : string;

  thedescription : string;


  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;

  view: CalendarView = CalendarView.Month;


  viewDate: Date = new Date();

  locale: string = 'es';

  weekStartsOn: number = DAYS_OF_WEEK.MONDAY;

  CalendarView = CalendarView;

  

  modalData: {
    action: string;
    event: CalendarEvent;
  };

  refresh = new Subject<void>();

  events: CalendarEvent[] = [];

  users : UserDTO[] = [];
  

  /*
  actions: CalendarEventAction[] = [
    {
      label: '<i class="fas fa-fw fa-pencil-alt"></i>',
      a11yLabel: 'Edit',
      onClick: ({ event }: { event: CalendarEvent }): void => {
        this.handleEvent('Edited', event);
      },
    },
    {
      label: '<i class="fas fa-fw fa-trash-alt"></i>',
      a11yLabel: 'Delete',
      onClick: ({ event }: { event: CalendarEvent }): void => {
        this.events = this.events.filter((iEvent) => iEvent !== event);
        this.handleEvent('Deleted', event);
      },
    },
  ];


  test_events: CalendarEvent[] = [ 
    {
      id: "1",
      start: subDays(startOfDay(new Date()), 1),
      end: addDays(new Date(), 1),
      title: 'A 3 day event',
      color: { ...colors.red },
      actions: this.actions,
      allDay: true,
      resizable: {
        beforeStart: true,
        afterEnd: true,
      },
      draggable: true,

      dayoff : false,
      description: "",
    },
    {
      id: "2",
      start: startOfDay(new Date()),
      title: 'An event with no end date',
      color: { ...colors.yellow },
      actions: this.actions,

      dayoff : false,
      description: "",
    },
    {
      id : "3",
      start: subDays(endOfMonth(new Date()), 3),
      end: addDays(endOfMonth(new Date()), 3),
      title: 'A long event that spans 2 months',
      color: { ...colors.blue },
      allDay: true,

      dayoff : false,
      description: "",
    },
    {
      id: "4",

      start: addHours(startOfDay(new Date()), 2),
      end: addHours(new Date(), 2),
      title: 'A draggable and resizable event',
      color: { ...colors.yellow },
      actions: this.actions,
      resizable: {
        beforeStart: true,
        afterEnd: true,
      },
      draggable: true,

      dayoff : false,
      description: "",
    }, 
  ];
*/
  activeDayIsOpen: boolean = true;


  days_offs = [];

  allRoles : string[] = [ "RELATIVE", "WORKER", "TRANSPORT", "ADMIN", "CLEANING", "NURSING" , "NURSING_ASSISTANT" , "LEGIONELLA_CONTROL" , "KITCHEN" , "MONITOR" , "SOCIAL_WORKER" , "PSYCHOLOGIST" , "MANAGER" , "PHYSIOTHERAPIST", "OCCUPATIONAL_THERAPIST", "OPERATOR_EXTRA_1" ];

  formatDate(thedate : Date)
  {
    return this.completeZeros(thedate.getDate()) + "/" + this.completeZeros(thedate.getMonth()+1) + "/" + (thedate.getFullYear()+"") +  " " + this.completeZeros(thedate.getHours()) + ":" + this.completeZeros(thedate.getMinutes()) ;
  }

  formatDate2(thedate : Date)
  {
    return (thedate.getFullYear()+"")  + "-" + this.completeZeros(thedate.getMonth()+1) + "-" + this.completeZeros(thedate.getDate()) + " "  + this.completeZeros(thedate.getHours()) + ":" + this.completeZeros(thedate.getMinutes()) + ":00" ;
  }

  



  completeZeros(x : number) : string
  {
    if(x<=9) return "0" + x;
    else return ""+x;
  }

  roleName(therole) { return rolName(therole); }

  getCalendarEvents()
  {
    this.days_offs = [];
    const currentRoles = this.authService.getRoles();

    if(hasRole(currentRoles, RoleCode.RELATIVE))
    {
      this.calendarService.getCalendarEventsForRelatives().subscribe(
        res => {
          this.events = [];
          this.events = res.map(x => this.wrapCalendarEvent(x));
          //this.events = this.events.concat(this.test_events);
          //console.log("this.events=>" +  JSON.stringify(this.events));
        },
        error => {
          console.error("getCalendarEventsForRelatives():"+JSON.stringify(error));
          this.toastService.show("No se han podido cargar los eventos del calendario.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );

        }
      );
    }
    else
    {
      this.calendarService.getCalendarEventsForWorkers().subscribe(
        res => {
          this.events = [];
          this.events = res.map(x => this.wrapCalendarEvent(x));
          //this.events = this.events.concat(this.test_events);
          //console.log("this.events=>" +  JSON.stringify(this.events));
        },
        error => {
          console.error("getCalendarEventsForWorkers():"+JSON.stringify(error));
          this.toastService.show("No se han podido cargar los eventos del calendario.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
        }
      );
    }

  }



  getAllUsers(roles? : string[])
  {
    if(!this.canModifyEvent()) return;

    if(!roles) roles = this.modalData.event.roles;

      this.usersService.getAllUsersByWorker(roles).subscribe(
        res => {
          this.users = res.map(x => this.wrapUser(x));
          //this.events = this.events.concat(this.test_events);
          console.log("this.users=>" +  JSON.stringify(this.users));
        },
        error => {
          console.error("getAllUsers():"+JSON.stringify(error));
          this.toastService.show("No se han podido cargar los usuarios.",
                                 "¡Ups!", 
                                 { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
                                );
          }
      );
  }
  

  wrapUser(x)
  {
    let wrapped : UserDTO = x;

    let rolenames : string[] = [];

    for(let coderole of x.roles)
      rolenames.push(this.roleName(coderole));

    wrapped.roles = rolenames;
      

    return wrapped;
  }



  wrapCalendarEvent(x)
  {
    let wrapped : CalendarEvent = x;

    wrapped.start = new Date(x.start);
    if(x.end) wrapped.end = new Date(x.end);
    if(x.publishdate) wrapped.publishdate = new Date(x.publishdate);

    if(wrapped.dayoff == true) 
      for (var d = new Date(wrapped.start); d <= wrapped.end; d.setDate(d.getDate() + 1)) 
        this.days_offs.push(new Date(d));
    

    return wrapped;
  }


  saveCalendarEvent(event : CalendarEvent)
  {
    if(!event.start) 
    { 
      this.toastService.show("Debes indicar un título para el evento.",
      "¡Ups!", 
      { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return; 
    }

    let fstart : string = this.formatDate2(event.start);
    let fend : string = event.end?this.formatDate2(event.end):null;
    let fpublishdate : string = event.publishdate?this.formatDate2(event.publishdate):null;

    if(event.end && event.end < event.start) 
    { 
      this.toastService.show("La fecha de inicio del evento no puede ser posterior a la de su fin.",
      "¡Ups!", 
      { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    } 

    this.processing = true;


    this.calendarService.saveCalendarEvent(event.id, fstart, fend, event.title, event.description, event.dayoff, event.roles, event.idsusers, fpublishdate).subscribe(
      res => {
        this.processing = false;
        this.toastService.show("Evento guardado correctamente.",
                               "¡Ok!", 
                               { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
                              );
        this.getCalendarEvents();
      },
      error => {
        this.processing = false;
        console.error("saveCalendarEvent():"+JSON.stringify(error));
        this.toastService.show("No se ha podido guardar el evento.",
                               "¡Ups!", 
                               { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
                              );

      }
    );

  }


  deleteCalendarEvent(event :CalendarEvent) {
    const dialogRef = this.dialogService.open(DeleteConfirmComponent, {
      autoFocus: true,
      hasScroll: true,
      closeOnBackdropClick: false,
      closeOnEsc: true,
      context: {
        text: 'Por favor, confirma que deseas eliminar el evento ',
        value: event.title 
      }
    });
    dialogRef.onClose.subscribe(
      res => {
        if (res === 'confirm') {
          this.processing = true;

          this.calendarService.deleteCalendarEvent(event.id).subscribe(
            result => {
              this.processing = false;

             //console.log("OK=>" + JSON.stringify(result));

             this.toastService.show("Evento eliminado correctamente.",
             "¡Ok!", 
             { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
            );
            this.getCalendarEvents();

            },
            error => {
              this.processing = false;


              console.error("getCalendarEventsForWorkers():"+JSON.stringify(error));
              this.toastService.show("No se ha podido eliminar el evento.",
                                     "¡Ups!", 
                                     { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
                                    );
      
            }
          );
        }
        else //cancelamos eliminación
        {
          this.handleEvent('Clicked', event);
        }
      }
    );
  }


  dayClicked({ date, events }: { date: Date; events: CalendarEvent[] }): void {
    if (isSameMonth(date, this.viewDate)) {
      if (
        (isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) ||
        events.length === 0
      ) {
        this.activeDayIsOpen = false;
      } else {
        this.activeDayIsOpen = true;
      }
      this.viewDate = date;
    }
  }

  eventTimesChanged({
    event,
    newStart,
    newEnd,
  }: CalendarEventTimesChangedEvent): void {
    this.events = this.events.map((iEvent) => {
      if (iEvent === event) {
        return {
          ...event,
          start: newStart,
          end: newEnd,
        };
      }
      return iEvent;
    });
    this.handleEvent('Dropped or resized', event);
  }

  handleEvent(action: string, event: CalendarEvent): void {
    console.log(JSON.stringify(event));
    this.modalData = { event, action };
    this.modal.open(this.modalContent, { size: 'lg' });
    //this.getAllUsers();
  }

  resetEvent()
  {
    this.getCalendarEvents();

  }

  addEvent(): void 
  {
    let newevent : CalendarEvent = {
      title: '',
      start: startOfDay(new Date()),
      end: endOfDay(new Date()),
      dayoff : false,
      description: ''

    };

    this.handleEvent('Clicked', newevent);

    /*
    this.events = [
      ...this.events,
      {
        title: '',
        start: startOfDay(new Date()),
        end: endOfDay(new Date()),
        color: colors.black,
        dayoff : false,
        description: '',

      },
    ];*/
  }

  deleteEvent(eventToDelete: CalendarEvent) {
    this.events = this.events.filter((event) => event !== eventToDelete);
  }

  setView(view: CalendarView) {
    this.view = view;
  }

  closeOpenMonthViewDay() {
    this.activeDayIsOpen = false;
  }

  /** MARCADO DE FESTIVOS */
  beforeMonthViewRender(renderEvent: CalendarMonthViewBeforeRenderEvent): void {
    renderEvent.body.forEach((day) => {
      if (this.isDayOff(day.date))   day.cssClass = 'bg-pink';
    });
  }

 
  isDayOff(d : Date) : boolean
  {
    const dayOfMonth = d.getDate();
    const month = d.getMonth();
    const year = d.getFullYear();

    if(d.getDay()==0 || d.getDay()==6) return true;
    
    for(let thedayoff of this.days_offs)
      if(thedayoff.getDate() == dayOfMonth && thedayoff.getMonth() == month && thedayoff.getFullYear() == year)
       return true;
    
    return false;
  }

  /** MARCADO DE FESTIVOS PARA SEMANA Y DÍA 
  beforeWeekViewRender(renderEvent: CalendarWeekViewBeforeRenderEvent) {
    renderEvent.hourColumns.forEach((hourColumn) => {
      hourColumn.hours.forEach((hour) => {
        hour.segments.forEach((segment) => {
          if (
            segment.date.getHours() >= 2 &&
            segment.date.getHours() <= 5 &&
            segment.date.getDay() === 2
          ) {
            segment.cssClass = 'bg-pink';
          }
        });
      });
    });
  }

  beforeDayViewRender(renderEvent: CalendarDayViewBeforeRenderEvent) {
    
    renderEvent.hourColumns.forEach((hourColumn) => {
      hourColumn.hours.forEach((hour) => {
        hour.segments.forEach((segment) => {
          if (segment.date.getHours() >= 2 && segment.date.getHours() <= 5) {
            segment.cssClass = 'bg-pink';
          }
        });
      });
    });
  }
*/

    canModifyEvent() : boolean
    {
      return this.authService.isAdmin() || this.authService.isManager();
    }


    formatGC(start : Date, end : Date)
    {
      if(!end) end = start;
      if(end.getFullYear()==start.getFullYear() && end.getMonth()==start.getMonth() && end.getDate()==start.getDate())
        end = new Date(start.getTime() + (1000 * 60 * 60 * 24));
      else
        end = new Date(end.getTime() + (1000 * 60 * 60 * 24));

  
  
      return (start.getFullYear()+"")  + this.completeZeros(start.getMonth()+1) +  this.completeZeros(start.getDate()) + "/" + 
             (end.getFullYear()+"")  + this.completeZeros(end.getMonth()+1) +  this.completeZeros(end.getDate());
    }
  
    exportGC(event : CalendarEvent) {
      const url = 'http://www.google.com/calendar/event?' +
      'action=TEMPLATE&dates=' + this.formatGC(event.start, event.end) +
      '&text=' + encodeURIComponent(event.title) +
      '&details=' + (event.description?encodeURIComponent(event.description):'');
      window.open(url, '_blank');
    }
  
    
    exportICS(event : CalendarEvent) 
    {
      if(!event.start) return;

      let thestart : Date = new Date( event.start );
      let theend : Date = event.end?new Date(event.end.getTime() + (1000 * 60 * 60 * 24)):new Date(thestart.getTime() + (1000 * 60 * 60 * 24));

      const blob = new Blob([
        'BEGIN:VCALENDAR\r\n' +
        'VERSION:2.0\r\n' +
        'BEGIN:VEVENT\r\n' +
        'URL:\r\n' +
        'DTSTART;VALUE=DATE:' + (thestart.getFullYear()+"")  + this.completeZeros(thestart.getMonth()+1) +  this.completeZeros(thestart.getDate()) + '\r\n' +
        'DTEND;VALUE=DATE:' + (theend.getFullYear()+"")  + this.completeZeros(theend.getMonth()+1) +  this.completeZeros(theend.getDate()) + '\r\n' +
        'SUMMARY:' + event.title + '\r\n' +
        'DESCRIPTION:' + (event.description?event.description:event.title) + '\r\n' +
        'LOCATION:\r\n' +
        'END:VEVENT\r\n' +
        'END:VCALENDAR\r\n'
      ], { type: 'text/plain' });
  
      const url = window.URL.createObjectURL(blob);

      const a = document.createElement('a');
      document.body.appendChild(a);
      a.setAttribute('style', 'display: none');
      a.setAttribute('download', 'event.ics');
      a.href = url;
      a.download = 'event.ics';
      a.click();
      window.URL.revokeObjectURL(url);
      a.remove();
    }

}
