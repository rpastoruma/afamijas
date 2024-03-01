import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 
import { LegionellaLogDTO } from 'src/app/shared/models/models';


@Injectable({
  providedIn: 'root'
})
export class LegionellalogsService {


  constructor(private http: HttpClient,) { }

  getLegionellaLogs(page : number, size: number,  dayfrom : Date, dayto : Date) 
  {
    let url = ENV.url.workers + `/getLegionellaLogs?page=${page}&size=${size}`;
    url += "&dayfrom=" + this.formatDate2(dayfrom);
    url += "&dayto=" + this.formatDate2(dayto);

    return this.http.get<any>(url, {});
  }
 

  registerLegionellaLog(theLegionellaLog : LegionellaLogDTO) 
  {
    const form = new FormData();

    if(theLegionellaLog.id && theLegionellaLog.id!='') form.append('id', theLegionellaLog.id);
    form.append('point', theLegionellaLog.point);
    if(theLegionellaLog.value) form.append('value', theLegionellaLog.value.toString());
    if(theLegionellaLog.temperature) form.append('temperature', theLegionellaLog.temperature.toString());
 
    return this.http.post<any>(ENV.url.workers + '/registerLegionellaLog', form);
  }
 

  deleteLegionellaLog(id : string) 
  {
    const form = new FormData();

    form.append('id', id);
 
    return this.http.post<any>(ENV.url.workers + '/deleteLegionellaLog', form);
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
