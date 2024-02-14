import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 


@Injectable({
  providedIn: 'root'
})
export class MenusService {

  constructor(private http: HttpClient,) { }

  getMenu(idpatient : string) 
  {
    let url = ENV.url.relatives + `/getMenu?idpatient=${idpatient}`;

    return this.http.get<any>(url, {});
  }
 


}