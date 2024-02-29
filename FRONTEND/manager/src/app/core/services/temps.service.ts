import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 
import { TempFridgeDTO, TempServiceDTO, MealSampleDTO } from 'src/app/shared/models/models';


@Injectable({
  providedIn: 'root'
})
export class TempsService 
{

  constructor(private http: HttpClient,) { }

  getTempFridges(page : number, size: number, dayfrom : Date, dayto : Date) 
  {
    let url = ENV.url.workers + `/getTempFridges?page=${page}&size=${size}`;
    if(dayfrom) url += "&dayfrom=" + this.formatDate2(dayfrom);
    if(dayto) url += "&dayto=" + this.formatDate2(dayto);

    return this.http.get<any>(url, {});
  }
 

  registerTempFridge(theTempFridge : TempFridgeDTO) 
  {
    const form = new FormData();

    if(theTempFridge.id && theTempFridge.id!='') form.append('id', theTempFridge.id);
    form.append('temperature', theTempFridge.temperature.toString());
 
    return this.http.post<any>(ENV.url.workers + '/registerTempFridge', form);
  }
 

  deleteTempFridge(id : string) 
  {
    const form = new FormData();

    form.append('id', id);
 
    return this.http.post<any>(ENV.url.workers + '/deleteTempFridge', form);
  }



  
  getTempServices(page : number, size: number, dayfrom : Date, dayto : Date) 
  {
    let url = ENV.url.workers + `/getTempServices?page=${page}&size=${size}`;
    if(dayfrom) url += "&dayfrom=" + this.formatDate2(dayfrom);
    if(dayto) url += "&dayto=" + this.formatDate2(dayto);

    return this.http.get<any>(url, {});
  }
 

  registerTempService(theTempService : TempServiceDTO) 
  {
    const form = new FormData();

    if(theTempService.id && theTempService.id!='') form.append('id', theTempService.id);
    form.append('dish', theTempService.dish);
    form.append('dish_type', theTempService.dish_type);
    if(theTempService.temperature_reception) form.append('temperature_reception', theTempService.temperature_reception.toString());
    if(theTempService.temperature_service) form.append('temperature_service', theTempService.temperature_service.toString());
 
    return this.http.post<any>(ENV.url.workers + '/registerTempService', form);
  }
 

  deleteTempService(id : string) 
  {
    const form = new FormData();

    form.append('id', id);
 
    return this.http.post<any>(ENV.url.workers + '/deleteTempService', form);
  }




  
  getMealSamples(page : number, size: number, dayfrom : Date, dayto : Date) 
  {
    let url = ENV.url.workers + `/getMealSamples?page=${page}&size=${size}`;
    if(dayfrom) url += "&dayfrom=" + this.formatDate2(dayfrom);
    if(dayto) url += "&dayto=" + this.formatDate2(dayto);

    return this.http.get<any>(url, {});
  }
 

  registerMealSample(theMealSample : MealSampleDTO) 
  {
    const form = new FormData();

    console.log("theMealSample===" + JSON.stringify(theMealSample));

    if(theMealSample.id && theMealSample.id!='') form.append('id', theMealSample.id);
    form.append('dish', theMealSample.dish);
    if(theMealSample.orgenolepticoOk || theMealSample.orgenolepticoOk==false) form.append('orgenolepticoOk', theMealSample.orgenolepticoOk.toString());
    if(theMealSample.cuerposExtraOk || theMealSample.cuerposExtraOk==false) form.append('cuerposExtraOk', theMealSample.cuerposExtraOk.toString());
    if(theMealSample.comments) form.append('comments', theMealSample.comments);
 
    return this.http.post<any>(ENV.url.workers + '/registerMealSample', form);
  }
 

  deleteMealSample(id : string) 
  {
    const form = new FormData();

    form.append('id', id);
 
    return this.http.post<any>(ENV.url.workers + '/deleteMealSample', form);
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