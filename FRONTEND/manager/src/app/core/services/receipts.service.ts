import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 
import { ReceiptDTO } from 'src/app/shared/models/models';

@Injectable({
  providedIn: 'root'
})
export class ReceiptsService {


  constructor(private http: HttpClient,) { }

  getReceipts(page : number, size: number, idmember : string, dayfrom : Date, dayto : Date, status : string) 
  {
    let url = ENV.url.workers + `/getReceipts?page=${page}&size=${size}`;
    if(idmember) url += "&idmember=" + idmember;
    if(dayfrom) url += "&dayfrom=" + this.formatDate2(dayfrom);
    if(dayto) url += "&dayto=" + this.formatDate2(dayto);
    if(status && status!='') url += "&status=" + status;

    return this.http.get<any>(url, {});
  }
 

  saveReceipt(theReceipt : ReceiptDTO) 
  {
    const form = new FormData();

    if(theReceipt.id != '') form.append('id', theReceipt.id);
    form.append('idmember', theReceipt.idmember);
    form.append('total', theReceipt.total.toString());
    form.append('url', theReceipt.url);
    form.append('duedate', this.formatDate2(theReceipt.duedate));
    if(theReceipt.paiddate)  form.append('paiddate', this.formatDate2(theReceipt.paiddate));
    form.append('status', theReceipt.status);
 
    return this.http.post<any>(ENV.url.workers + '/saveReceipt', form);
  }
 

  deleteReceipt(id : string) 
  {
    const form = new FormData();

    form.append('id', id);
 
    return this.http.post<any>(ENV.url.workers + '/deleteReceipt', form);
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
