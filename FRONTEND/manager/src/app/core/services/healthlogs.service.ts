import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 
import { HealthLogDTO } from 'src/app/shared/models/models';

@Injectable({
  providedIn: 'root'
})
export class HealthlogsService {


  constructor(private http: HttpClient,) { }

  getHealthLogs(page : number, size: number, groupcode : string, idpatient : string, dayfrom : Date, dayto : Date) 
  {
    let url = ENV.url.workers + `/getHealthLogs?page=${page}&size=${size}`;
    if(groupcode && groupcode != '') url += "&groupcode=" + groupcode;
    if(idpatient && idpatient != '') url += "&idpatient=" + idpatient;
    url += "&dayfrom=" + this.formatDate2(dayfrom);
    url += "&dayto=" + this.formatDate2(dayto);

    return this.http.get<any>(url, {});
  }
 

  registerHealthLog(theHealthLog : HealthLogDTO) 
  {
    const form = new FormData();

    if(theHealthLog.id && theHealthLog.id!='') form.append('id', theHealthLog.id);
    form.append('idpatient', theHealthLog.idpatient);
    if(theHealthLog.low_pressure && theHealthLog.low_pressure!=0) form.append('low_pressure', theHealthLog.low_pressure.toString());
    if(theHealthLog.high_pressure && theHealthLog.high_pressure!=0) form.append('high_pressure', theHealthLog.high_pressure.toString());
    if(theHealthLog.sugar && theHealthLog.sugar!=0) form.append('sugar', theHealthLog.sugar.toString());
 
    return this.http.post<any>(ENV.url.workers + '/registerHealthLog', form);
  }
 

  deleteHealthLog(id : string) 
  {
    const form = new FormData();

    form.append('id', id);
 
    return this.http.post<any>(ENV.url.workers + '/deleteHealthLog', form);
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
