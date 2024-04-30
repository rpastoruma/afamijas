 import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 


@Injectable({
  providedIn: 'root'
})
export class MembersService {

  constructor(private http: HttpClient,) { }

  getMembers(page : number, size : number, membernumber? : number, name_surnames? : string, documentid? : string, status? : string) {
    let url = ENV.url.members + `/getMembers?page=${page}&size=${size}`;
    if(membernumber) url += '&membernumber=' + membernumber;
    if(name_surnames) url += '&name_surnames=' + name_surnames;
    if(documentid) url += '&documentid=' + documentid;
    if(status) url += '&status=' + status;
    return this.http.get<any>(url, {});
  }


  
}
