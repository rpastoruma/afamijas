import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 


@Injectable({
  providedIn: 'root'
})
export class RelativeAbsencesService {

  constructor(private http: HttpClient,) { }

  getRelativeAbsences(page : number, size: number, idpatient : string, from? : Date, to? : Date) 
  {
    let url = ENV.url.relatives + `/getRelativeAbsences?page=${page}&size=${size}&idpatient=${idpatient}`;
    if(from) url += "&from=" + this.formatDate2(from);
    if(to) url += "&to=" + this.formatDate2(to);

    return this.http.get<any>(url, {});
  }
 

  saveAbsenceByRelative(id : string, idpatient : string, from : Date, to : Date, allday : boolean, transport : string, comment : string) 
  {
    const form = new FormData();
    if(id && id!='') form.append('id', id);
    form.append('idpatient', idpatient);
    form.append('from', this.formatDate1(from));
    form.append('to', this.formatDate1(to));
    form.append('transport', transport.toString());
    form.append('allday', allday.toString());
    if(comment && comment!='') form.append('comment', comment);
 
    return this.http.post<any>(ENV.url.relatives + '/saveAbsenceByRelative', form);
  }

  deleteAbsence(idabsence : string, idpatient : string) 
  {
    const form = new FormData();
    form.append('idabsence', idabsence);
    form.append('idpatient', idpatient);
 
    return this.http.post<any>(ENV.url.relatives + '/deleteAbsence', form);
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