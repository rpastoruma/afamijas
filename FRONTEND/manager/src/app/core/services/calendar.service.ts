import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 
import { throwError as observableThrowError, Observable, catchError } from 'rxjs';
import { CalendarEvent } from 'angular-calendar';


@Injectable({
  providedIn: 'root'
})
export class CalendarService {

  constructor(private http: HttpClient,) { }

  
  getCalendarEventsForRelatives() 
  {
    const url = ENV.url.relatives + `/getCalendarEvents`;
    return this.http.get<any>(url, {});
   }

   getCalendarEventsForWorkers() 
  {
    const url = ENV.url.workers + `/getCalendarEvents`;
    return this.http.get<any>(url, {});
   }


   saveCalendarEvent(
    idcalendarevent : string | null, 
    start : string, 
    end : string | null, 
    title : string, 
    description : string | null, 
    dayoff : boolean, 
    roles : string[] | null, 
    idsusers : string[] | null,
    publishdate : string | null, 

    ) 
   {
     const form = new FormData();
     if(idcalendarevent) form.append('idcalendarevent', idcalendarevent);
     if(start) form.append('start', start);
     if(end) form.append('end', end);
     if(title) form.append('title', title);
     if(description) form.append('description', description);
     if(dayoff) form.append('dayoff', ""+dayoff);
     if(roles) form.append('roles', roles.toString());
     if(idsusers) form.append('idsusers', idsusers.toString());
     if(publishdate) form.append('publishdate', publishdate);
 
     return this.http.post<any>(ENV.url.workers + '/saveCalendarEvent', form);

    }



    deleteCalendarEvent(idcalendarevent : string)
    {
      const form = new FormData();
      form.append('idcalendarevent', idcalendarevent);

      return this.http.post<any>(ENV.url.workers + '/deleteCalendarEvent', form);
     }
}