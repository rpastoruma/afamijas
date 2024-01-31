import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 


@Injectable({
  providedIn: 'root'
})
export class UsersService {

  constructor(private http: HttpClient,) { }

  
  getAllUsers(roles : string[]) 
  {
    if(!roles) roles = [];
    const url = ENV.url.workers + `/getAllUsers?roles=${roles.toString()}`;
    return this.http.get<any>(url, {});
   }


}