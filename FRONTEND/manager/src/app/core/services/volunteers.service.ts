 import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 
import { VolunteerDTO } from 'src/app/shared/models/models';


@Injectable({
  providedIn: 'root'
})
export class VolunteersService {


  constructor(private http: HttpClient,) { }

  getVolunteers(page : number, size : number, name_surnames? : string, documentid? : string, status? : string) {
    let url = ENV.url.volunteers + `/getVolunteers?page=${page}&size=${size}`;
    if(name_surnames) url += '&name_surnames=' + name_surnames;
    if(documentid) url += '&documentid=' + documentid;
    if(status) url += '&status=' + status;
    return this.http.get<any>(url, {});
  }

  saveVolunteer(theVolunteer: VolunteerDTO, is_document_signed : boolean) 
  {
    const form = new FormData();
    if(theVolunteer.id && theVolunteer.id!='') form.append('id', theVolunteer.id);
    form.append('name', theVolunteer.name);
    form.append('surname1', theVolunteer.surname1);
    form.append('email', theVolunteer.email);
    form.append('documentid', theVolunteer.documentid);
    form.append('documenttype', theVolunteer.documenttype);

    if(theVolunteer.surname2 && theVolunteer.surname2!='') form.append('surname2', theVolunteer.surname2);
    if(theVolunteer.phone && theVolunteer.phone!='') form.append('phone', theVolunteer.phone);
    form.append('is_document_signed', is_document_signed.toString());

    if(is_document_signed && theVolunteer.register_document_url_signed && theVolunteer.register_document_url_signed!='')
      form.append('register_document_url', theVolunteer.register_document_url_signed);
    else
    {
      if(theVolunteer.register_document_url && theVolunteer.register_document_url!='') 
        form.append('register_document_url', theVolunteer.register_document_url);
    }


    
    if(theVolunteer.postaladdress && theVolunteer.postaladdress!='') form.append('postaladdress', theVolunteer.postaladdress);
    if(theVolunteer.idcity) form.append('idcity', theVolunteer.idcity.toString());
    if(theVolunteer.idstate) form.append('idstate', theVolunteer.idstate.toString());
    if(theVolunteer.idcountry) form.append('idcountry', theVolunteer.idcountry.toString());
    if(theVolunteer.postalcode && theVolunteer.postalcode!='') form.append('postalcode', theVolunteer.postalcode);

 
    return this.http.post<any>(ENV.url.volunteers + '/saveVolunteer', form);
  }


  unregisterVolunteer(id: string, unregister_reason: string, unregister_document_url: string, is_document_signed: boolean) 
  {
    const form = new FormData();
    form.append('id', id);
    if(unregister_reason) form.append('unregister_reason', unregister_reason);
    if(unregister_document_url && unregister_document_url!='') form.append('unregister_document_url', unregister_document_url);
    if(is_document_signed) form.append('is_document_signed', is_document_signed.toString());

 
    return this.http.post<any>(ENV.url.volunteers + '/unregisterVolunteer', form);
  }
  

  signDocumentRegister(idvolunteer: string, register_document_url_signed: string) {
    const form = new FormData();
    form.append('idvolunteer', idvolunteer);
    form.append('register_document_url_signed', register_document_url_signed);

    return this.http.post<any>(ENV.url.volunteers + '/signDocumentRegister', form);
  }
  

  signDocumentUnRegister(idvolunteer: string, unregister_document_url_signed: string) {
    const form = new FormData();
    form.append('idvolunteer', idvolunteer);
    form.append('unregister_document_url_signed', unregister_document_url_signed);

    return this.http.post<any>(ENV.url.volunteers + '/signDocumentUnRegister', form);
  }


  
}
