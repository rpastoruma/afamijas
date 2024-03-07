import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 
import { WCLogDTO } from 'src/app/shared/models/models';


@Injectable({
  providedIn: 'root'
})
export class WclogsService {


  constructor(private http: HttpClient,) { }

  getWCLogs(page : number, size: number,  dayfrom : Date, dayto : Date) 
  {
    let url = ENV.url.workers + `/getWCLogs?page=${page}&size=${size}`;
    url += "&dayfrom=" + this.formatDate2(dayfrom);
    url += "&dayto=" + this.formatDate2(dayto);

    return this.http.get<any>(url, {});
  }
 

  registerWCLog(theWCLog : WCLogDTO) 
  {
    const form = new FormData();

    if(theWCLog.id && theWCLog.id!='') form.append('id', theWCLog.id);
    form.append('point', theWCLog.point);
    if(theWCLog.hour) form.append('hour', theWCLog.hour);
 
    return this.http.post<any>(ENV.url.workers + '/registerWCLog', form);
  }
 

  deleteWCLog(id : string) 
  {
    const form = new FormData();

    form.append('id', id);
 
    return this.http.post<any>(ENV.url.workers + '/deleteWCLog', form);
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
