import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class NotificationsService {

  constructor(private http: HttpClient) { }

  findMyNotifications() {
    const url = ENV.url.notifications + `/findMyNotifications`;
    return this.http.get<any>(url, {});
  }

  markAsRead(ids: string) {
    const form = new FormData();
    if (ids) { form.append('ids', ids); }

    return this.http.post<any>(ENV.url.notifications + '/markAsRead', form);
  }
}
