import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 
import { InvoiceDTO } from 'src/app/shared/models/models';

@Injectable({
  providedIn: 'root'
})
export class InvoicesService {


  constructor(private http: HttpClient,) { }

  getInvoices(page : number, size: number, idpatient : string, dayfrom : Date, dayto : Date, status : string) 
  {
    let url = ENV.url.workers + `/getInvoices?page=${page}&size=${size}`;
    if(idpatient) url += "&idpatient=" + idpatient;
    if(dayfrom) url += "&dayfrom=" + this.formatDate2(dayfrom);
    if(dayto) url += "&dayto=" + this.formatDate2(dayto);
    if(status && status!='') url += "&status=" + status;

    return this.http.get<any>(url, {});
  }
 

  saveInvoice(theInvoice : InvoiceDTO) 
  {
    const form = new FormData();

    if(theInvoice.id != '') form.append('id', theInvoice.id);
    form.append('idpatient', theInvoice.idpatient);
    form.append('total', theInvoice.total.toString());
    form.append('url', theInvoice.url);
    form.append('duedate', this.formatDate2(theInvoice.duedate));
    if(theInvoice.paiddate)  form.append('paiddate', this.formatDate2(theInvoice.paiddate));
    form.append('status', theInvoice.status);
 
    return this.http.post<any>(ENV.url.workers + '/saveInvoice', form);
  }
 

  deleteInvoice(id : string) 
  {
    const form = new FormData();

    form.append('id', id);
 
    return this.http.post<any>(ENV.url.workers + '/deleteInvoice', form);
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
