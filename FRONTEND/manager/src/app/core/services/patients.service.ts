
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 
import { PatientDTO } from 'src/app/shared/models/models';


@Injectable({
  providedIn: 'root'
})
export class PatientsService {


  constructor(private http: HttpClient,) { }

  getPatients(page : number, size : number, name_surnames? : string, documentid? : string, status? : string) {
    let url = ENV.url.patients + `/getPatients?page=${page}&size=${size}`;
    if(name_surnames) url += '&name_surnames=' + name_surnames;
    if(documentid) url += '&documentid=' + documentid;
    if(status) url += '&status=' + status;
    return this.http.get<any>(url, {});
  }

  savePatient(thePatient: PatientDTO) 
  {
    const form = new FormData();
    if(thePatient.id && thePatient.id!='') form.append('id', thePatient.id);
    form.append('name', thePatient.name);
    form.append('surname1', thePatient.surname1);
    if(thePatient.surname2) form.append('surname2', thePatient.surname2);
    form.append('birthdate', this.formatDate2(thePatient.birthdate));
    form.append('documentid', thePatient.dni);
    form.append('documenttype', thePatient.documenttype);

    if(thePatient.register_document_url_signed && thePatient.register_document_url_signed!='')
    {
      form.append('register_document_url', thePatient.register_document_url_signed);
      form.append('is_document_signed', 'true')
    }
    else if(thePatient.register_document_url && thePatient.register_document_url!='')
    {
      form.append('register_document_url', thePatient.register_document_url);
      form.append('is_document_signed', 'false')
    }

    form.append('servicetype', thePatient.servicetype);
    form.append('tallerpsico', thePatient.tallerpsico.toString());

    if(thePatient.postaladdress && thePatient.postaladdress!='') form.append('postaladdress', thePatient.postaladdress);
    if(thePatient.idcity) form.append('idcity', thePatient.idcity.toString());
    if(thePatient.idstate) form.append('idstate', thePatient.idstate.toString());
    if(thePatient.idcountry) form.append('idcountry', thePatient.idcountry.toString());
    if(thePatient.postalcode && thePatient.postalcode!='') form.append('postalcode', thePatient.postalcode);


    return this.http.post<any>(ENV.url.patients + '/savePatient', form);
  }


  unregisterPatient(id: string, unregister_reason: string, unregister_document_url: string, is_document_signed: boolean) 
  {
    const form = new FormData();
    form.append('id', id);
    if(unregister_reason) form.append('unregister_reason', unregister_reason);
    if(unregister_document_url && unregister_document_url!='') form.append('unregister_document_url', unregister_document_url);
    if(is_document_signed) form.append('is_document_signed', is_document_signed.toString());

 
    return this.http.post<any>(ENV.url.patients + '/unregisterPatient', form);
  }
  

  signDocumentRegister(idpatient: string, register_document_url_signed: string) {
    const form = new FormData();
    form.append('idpatient', idpatient);
    form.append('register_document_url_signed', register_document_url_signed);

    return this.http.post<any>(ENV.url.patients + '/signDocumentRegister', form);
  }
  

  signDocumentUnRegister(idpatient: string, unregister_document_url_signed: string) {
    const form = new FormData();
    form.append('idpatient', idpatient);
    form.append('unregister_document_url_signed', unregister_document_url_signed);

    return this.http.post<any>(ENV.url.patients + '/signDocumentUnRegister', form);
  }


  
  
  formatDate1(thedate : Date)
  {
    return (thedate.getFullYear()+"")  + "-" + this.completeZeros(thedate.getMonth()+1) + "-" + this.completeZeros(thedate.getDate()) + " " + this.completeZeros(thedate.getHours()) + ":" + this.completeZeros(thedate.getMinutes());
  }

  
  formatDate2(thedate : Date)
  {
    return (thedate.getFullYear()+"")  + "-" + this.completeZeros(thedate.getMonth()+1) + "-" + this.completeZeros(thedate.getDate());
  }


  completeZeros(x : number) : string
  {
    if(x<=9) return "0" + x;
    else return ""+x;
  }

}
