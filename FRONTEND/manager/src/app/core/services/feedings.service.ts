import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 
import { FeedingDTO } from 'src/app/shared/models/models';


@Injectable({
  providedIn: 'root'
})
export class FeedingsService {


  constructor(private http: HttpClient,) { }

  getFeedings(page : number, size: number, groupcode : string, idpatient : string, day : Date) 
  {
    let url = ENV.url.workers + `/getFeedings?page=${page}&size=${size}`;
    if(groupcode && groupcode != '') url += "&groupcode=" + groupcode;
    if(idpatient && idpatient != '') url += "&idpatient=" + idpatient;
    if(day) url += "&day=" + this.formatDate2(day);

    return this.http.get<any>(url, {});
  }
 

  registerFeeding(theFeeding : FeedingDTO) 
  {
    const form = new FormData();

    if(theFeeding.id && theFeeding.id!='') form.append('id', theFeeding.id);
    form.append('idpatient', theFeeding.idpatient);
    form.append('daymeal', theFeeding.daymeal);
    form.append('dish', theFeeding.dish);
    form.append('result', theFeeding.result);
    if(theFeeding.indications && theFeeding.indications!='') form.append('indications', theFeeding.indications);
    if(theFeeding.incidences && theFeeding.incidences!='') form.append('incidences', theFeeding.incidences);
 
    return this.http.post<any>(ENV.url.workers + '/registerFeeding', form);
  }
 

  deleteFeeding(id : string) 
  {
    const form = new FormData();

    form.append('id', id);
 
    return this.http.post<any>(ENV.url.workers + '/deleteFeeding', form);
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
