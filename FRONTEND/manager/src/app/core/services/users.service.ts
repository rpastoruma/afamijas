import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 
import { WorkerDTO } from 'src/app/shared/models/models';


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
  
  getAllUsersByRoles(roles : string[]) 
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

  getAllWorkers() {
    let url = ENV.url.workers + `/getAllWorkers`;
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

  
  getWorkers(page : number, size : number, role? : string, name_surnames? : string, documentid? : string, status? : string) {

console.log("ROL=" + JSON.stringify(role));

    let url = ENV.url.workers + `/getWorkers?page=${page}&size=${size}`;
    if(role) url += '&role=' + role;
    if(name_surnames) url += '&name_surnames=' + name_surnames;
    if(documentid) url += '&documentid=' + documentid;
    if(status) url += '&status=' + status;
    return this.http.get<any>(url, {});
  }

  



    saveWorker(theWorker: WorkerDTO) 
    {
      const form = new FormData();
      if(theWorker.id && theWorker.id!='') form.append('id', theWorker.id);
      form.append('roles', theWorker.roles.toString());
      form.append('name', theWorker.name);
      form.append('surname1', theWorker.surname1);
      form.append('email', theWorker.email);
      form.append('documentid', theWorker.documentid);
      form.append('documenttype', theWorker.documenttype);

      if(theWorker.surname2 && theWorker.surname2!='') form.append('surname2', theWorker.surname2);
      if(theWorker.password && theWorker.password!='') form.append('password', theWorker.password);
      if(theWorker.phone && theWorker.phone!='') form.append('phone', theWorker.phone);

      if(theWorker.postaladdress && theWorker.postaladdress!='') form.append('postaladdress', theWorker.postaladdress);
      if(theWorker.postalcode && theWorker.postalcode!='') form.append('postalcode', theWorker.postalcode);
      if(theWorker.idcity) form.append('idcity', theWorker.idcity.toString());
      if(theWorker.idstate) form.append('idstate', theWorker.idstate.toString());
      if(theWorker.idcountry) form.append('idcountry', theWorker.idcountry.toString());

      if(theWorker.nss && theWorker.nss!='') form.append('nss', theWorker.nss);
      if(theWorker.categoria_profesional && theWorker.categoria_profesional!='') form.append('categoria_profesional', theWorker.categoria_profesional);
      if(theWorker.tipo_contrato && theWorker.tipo_contrato!='') form.append('tipo_contrato', theWorker.tipo_contrato);
      if(theWorker.jornada_laboral && theWorker.jornada_laboral!='') form.append('jornada_laboral', theWorker.jornada_laboral);
      if(theWorker.horario && theWorker.horario!='') form.append('horario', theWorker.horario);

      return this.http.post<any>(ENV.url.workers + '/saveWorker', form);

    }


    
  unregisterWorker(id: string) 
  {
    const form = new FormData();
    form.append('id', id);

 
    return this.http.post<any>(ENV.url.workers + '/unregisterWorker', form);
  }
  

  getAddressBook(page: number, size: number, type?: string, fullname?: string, phone?: string, email?: string) {
    let url = ENV.url.workers + `/getAddressBook?page=${page}&size=${size}`;
    
    if (type) url += `&type=${encodeURIComponent(type)}`;
    if (fullname) url += `&fullname=${encodeURIComponent(fullname)}`;
    if (phone) url += `&phone=${encodeURIComponent(phone)}`;
    if (email) url += `&email=${encodeURIComponent(email)}`;
  
    return this.http.get<any>(url, {});
  }

  saveAddressBook(id : string, fullname: string, phone?: string, email?: string, observations?: string) {
    const form = new FormData();
    
    if (id && id!='') form.append('id', id);
    form.append('fullname', fullname);
    if (phone) form.append('phone', phone);
    if (email) form.append('email', email);
    if (observations) form.append('observations', observations);
  
    return this.http.post<any>(ENV.url.workers + '/saveAddressBook', form);
  }

  deleteAddressBook(id: string) {
    const form = new FormData();
    form.append('id', id);
  
    return this.http.post<any>(ENV.url.workers + '/deleteAddressBook', form);
  }
  

  

}