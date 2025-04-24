import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 
import { DocDTO, DocPsicoDTO } from 'src/app/shared/models/models';

@Injectable({
  providedIn: 'root'
})
export class DocsService {


  constructor(private http: HttpClient,) { }

  getDocs(page : number, size: number, dayfrom : Date, dayto : Date, theText : string) 
  {
    let url = ENV.url.workers + `/getDocs?page=${page}&size=${size}`;
    if(dayfrom) url += "&dayfrom=" + this.formatDate2(dayfrom);
    if(dayto) url += "&dayto=" + this.formatDate2(dayto);
    if(theText) url += "&text=" + theText;

    return this.http.get<any>(url, {});
  }
 

  saveDoc(theDoc : DocDTO, createEvent : boolean) 
  {
    const form = new FormData();

    if(theDoc.id != '') form.append('id', theDoc.id);
    form.append('url', theDoc.url);
    form.append('title', theDoc.title);
    if(theDoc.description && theDoc.description!='') form.append('description', theDoc.description);
    if(theDoc.dayfrom)  form.append('dayfrom', this.formatDate2(theDoc.dayfrom));
    if(theDoc.dayto)  form.append('dayto', this.formatDate2(theDoc.dayto));
    if(theDoc.roles && theDoc.roles.length>0)  form.append('roles', theDoc.roles.toString());
    if(createEvent==true) form.append('createEvent', 'true');
 
    return this.http.post<any>(ENV.url.workers + '/saveDoc', form);
  }
 

 

  deleteDoc(id : string) 
  {
    const form = new FormData();

    form.append('id', id);
 
    return this.http.post<any>(ENV.url.workers + '/deleteDoc', form);
  }



  getDocsPsico(page : number, size: number, idpatient : string, type : string, theText : string) 
  {
    let url = ENV.url.workers + `/getDocsPsico?page=${page}&size=${size}`;
    if(idpatient) url += "&idpatient=" + idpatient;
    if(type) url += "&type=" + type;
    if(theText) url += "&text=" + theText;

    return this.http.get<any>(url, {});
  }
 

  saveDocPsico(theDoc : DocPsicoDTO) 
  {
    const form = new FormData();

    if(theDoc.id != '') form.append('id', theDoc.id);
    form.append('url', theDoc.url);
    form.append('idpatient', theDoc.idpatient);
    form.append('type', theDoc.type);
    if(theDoc.description && theDoc.description!='') form.append('description', theDoc.description);

    return this.http.post<any>(ENV.url.workers + '/saveDocPsico', form);
  }

  
  deleteDocPsico(id : string) 
  {
    const form = new FormData();

    form.append('id', id);
 
    return this.http.post<any>(ENV.url.workers + '/deleteDocPsico', form);
  }

  formatDate2(thedate : Date)
  {
    if(thedate==null) return null;
    return (thedate.getFullYear()+"")  + "-" + this.completeZeros(thedate.getMonth()+1) + "-" + this.completeZeros(thedate.getDate());
  }


  completeZeros(x : number) : string
  {
    if(x<=9) return "0" + x;
    else return ""+x;
  }


}
