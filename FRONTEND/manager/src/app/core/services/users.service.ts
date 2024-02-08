import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 


@Injectable({
  providedIn: 'root'
})
export class UsersService {

  constructor(private http: HttpClient,) { }

  
  getAllUsersByWorker(roles : string[]) 
  {
    if(!roles) roles = [];
    const url = ENV.url.workers + `/getAllUsers?roles=${roles.toString()}`;
    return this.http.get<any>(url, {});
   }


   getPatientsByRelative() 
   {
     const url = ENV.url.relatives + `/getPatients`;
     return this.http.get<any>(url, {});
    }
 

}