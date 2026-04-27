import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 
import { ContratoDTO, NominaDTO } from 'src/app/shared/models/models';

@Injectable({
  providedIn: 'root'
})
export class ContratosService {


  constructor(private http: HttpClient,) { }

  getContratos(page : number, size: number, idworker : string, dayfrom : Date, dayto : Date) 
  {
    let url = ENV.url.workers + `/getContratos?page=${page}&size=${size}`;
    if(idworker) url += "&idworker=" + idworker;
    if(dayfrom) url += "&dayfrom=" + this.formatDate2(dayfrom);
    if(dayto) url += "&dayto=" + this.formatDate2(dayto);
    if(status && status!='') url += "&status=" + status;

    return this.http.get<any>(url, {});
  }
 

  saveContrato(theContrato : ContratoDTO) 
  {
    const form = new FormData();

    if(theContrato.id != '') form.append('id', theContrato.id);
    form.append('idworker', theContrato.idworker);
    form.append('url', theContrato.url);
    form.append('startdate', this.formatDate2(theContrato.startdate )); 
    if(theContrato.enddate) form.append('enddate', this.formatDate2(theContrato.enddate )); 
 
    return this.http.post<any>(ENV.url.workers + '/saveContrato', form);
  }
 

  
  deleteContrato(id : string) 
  {
    const form = new FormData();

    form.append('id', id);
 
    return this.http.post<any>(ENV.url.workers + '/deleteContrato', form);
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
