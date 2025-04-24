import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 
import { NominaDTO } from 'src/app/shared/models/models';

@Injectable({
  providedIn: 'root'
})
export class NominasService {


  constructor(private http: HttpClient,) { }

  getNominas(page : number, size: number, idworker : string, dayfrom : Date, dayto : Date) 
  {
    let url = ENV.url.workers + `/getNominas?page=${page}&size=${size}`;
    if(idworker) url += "&idworker=" + idworker;
    if(dayfrom) url += "&dayfrom=" + this.formatDate2(dayfrom);
    if(dayto) url += "&dayto=" + this.formatDate2(dayto);
    if(status && status!='') url += "&status=" + status;

    return this.http.get<any>(url, {});
  }
 

  saveNomina(theNomina : NominaDTO) 
  {
    const form = new FormData();

    if(theNomina.id != '') form.append('id', theNomina.id);
    form.append('idworker', theNomina.idworker);
    form.append('url', theNomina.url);
    form.append('duedate', this.formatDate2(theNomina.duedate));
 
    return this.http.post<any>(ENV.url.workers + '/saveNomina', form);
  }
 

  deleteNomina(id : string) 
  {
    const form = new FormData();

    form.append('id', id);
 
    return this.http.post<any>(ENV.url.workers + '/deleteNomina', form);
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
