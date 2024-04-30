import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV } from '../../environments/environment';


@Injectable({
  providedIn: 'root'
})
export class NotificationsService {

  constructor(private http: HttpClient) { }

    
  findMyNotifications() 
  {

      let url = ENV.url.notifications + `/findMyNotifications`;
      return this.http.get<any>(url, {});
  }


  
  deleteAll() 
  {
    const form = new FormData();
    return this.http.post<any>(ENV.url.notifications + '/deleteAll', form);
  }

  
  
  delete(id: string) 
  {
    const form = new FormData();
    form.append('id', id);
    return this.http.post<any>(ENV.url.notifications + '/delete', form);
  }

  


}
