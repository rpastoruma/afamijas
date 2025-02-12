import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 
import { AtencionDTO } from 'src/app/shared/models/models';

@Injectable({
  providedIn: 'root'
})
export class AtencionesService {


  constructor(private http: HttpClient,) { }

  getAtenciones(page : number, size: number, dayfrom : Date, dayto : Date) 
  {
    let url = ENV.url.workers + `/getAtenciones?page=${page}&size=${size}`;
    if(dayfrom) url += "&dayfrom=" + this.formatDate2(dayfrom);
    if(dayto) url += "&dayto=" + this.formatDate2(dayto);

    return this.http.get<any>(url, {});
  }
 

  registerAtencion(theAtencion : AtencionDTO) 
  {
    const form = new FormData();

    if(theAtencion.id != '') form.append('id', theAtencion.id);
    if(theAtencion.number != '') form.append('number', theAtencion.number);
    form.append('datedone', this.formatDate2(theAtencion.datedone));
    form.append('clientfullname', theAtencion.clientfullname);
    form.append('sex', theAtencion.sex);
    form.append('nationality', theAtencion.nationality);
    form.append('relationship', theAtencion.relationship);
    form.append('why', theAtencion.why);
    form.append('via', theAtencion.via);
    if(theAtencion.professional != '') form.append('professional', theAtencion.professional);
    if(theAtencion.observations != '') form.append('observations', theAtencion.observations);
 
    return this.http.post<any>(ENV.url.workers + '/registerAtencion', form);
  }
 

  deleteAtencion(id : string) 
  {
    const form = new FormData();

    form.append('id', id);
 
    return this.http.post<any>(ENV.url.workers + '/deleteAtencion', form);
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
