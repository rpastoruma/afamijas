import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 


@Injectable({
  providedIn: 'root'
})
export class RoutesService {

  constructor(private http: HttpClient,) { }

   getRouteByPatientForRelative(idpatient : string) 
   {
     const url = ENV.url.relatives + `/getRoute?idpatient=${idpatient}`;
     return this.http.get<any>(url, {});
    }
 

    

    changeRouteStop(
    idpatient : string, 
    idroutestop : string, 
    from : string | null, 
    to : string | null
    ) 
   {
     const form = new FormData();
     form.append('idpatient', idpatient);
     form.append('idroutestop', idroutestop);
     if(from) form.append('from', from);
     if(to) form.append('to', to);
 
     return this.http.post<any>(ENV.url.relatives + '/changeRouteStop', form);

    }





}