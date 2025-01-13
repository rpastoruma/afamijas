import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 


@Injectable({
  providedIn: 'root'
})
export class UsersService {


  constructor(private http: HttpClient,) { }

  //FAMILIARES

  getPatientsByRelative() 
  {
    const url = ENV.url.relatives + `/getPatients`;
    return this.http.get<any>(url, {});
  }


  //TRABAJADORES
  
  getAllUsersByWorker(roles : string[]) 
  {
    if(!roles) roles = [];
    const url = ENV.url.workers + `/getAllUsers?roles=${roles.toString()}`;
    return this.http.get<any>(url, {});
  }

  getAllPatients(groupcode? : string) {
    let url = ENV.url.workers + `/getAllPatients`;
    if(groupcode) url += '?groupcode=' + groupcode;
    return this.http.get<any>(url, {});
  }

  getPatientById(id : string) {
    let url = ENV.url.workers + `/getPatientById?id=` +  id;
    return this.http.get<any>(url, {});
 }
  

  getAllMembers() {
    let url = ENV.url.workers + `/getAllMembers`;
    return this.http.get<any>(url, {});
  }

  getMedications(idpatient : string, page :number, size: number)
  {
    let url = ENV.url.workers + `/getMedications?page=${page}&size=${size}`;
    if(idpatient != '') url += '&idpatient=' + idpatient;

    return this.http.get<any>(url, {});
  }
 
  modifyMedication(idpatient: string, medication_description_morning: string, medication_description_evening: string) 
  {
    const form = new FormData();
    form.append('idpatient', idpatient);
    form.append('medication_description_morning', medication_description_morning);
    form.append('medication_description_evening', medication_description_evening);

    return this.http.post<any>(ENV.url.workers + '/modifyMedication', form);
  }


  getFoods(idpatient : string, page :number, size: number) 
  {
    let url = ENV.url.workers + `/getFoods?page=${page}&size=${size}`;
    if(idpatient != '') url += '&idpatient=' + idpatient;

    return this.http.get<any>(url, {});
  }

  modifyFood(idpatient: string, menu_type: string, breakfast_description: string) 
  {
    const form = new FormData();
    form.append('idpatient', idpatient);
    form.append('menu_type', menu_type);
    form.append('breakfast_description', breakfast_description);

    return this.http.post<any>(ENV.url.workers + '/modifyFood', form);
  }


}