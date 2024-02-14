import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 


@Injectable({
  providedIn: 'root'
})
export class PermissionsService {

  constructor(private http: HttpClient,) { }

  getPermissions(page : number, size: number, idpatient : string, status :string) 
  {
    let url = ENV.url.relatives + `/getPermissions?page=${page}&size=${size}&idpatient=${idpatient}`;
    if(status) url += "&status=" + status;

    return this.http.get<any>(url, {});
  }
 
  signPermission(idpatient : string, idpermission : string, signedfileurl: string) 
  {
    const form = new FormData();
    form.append('idpatient', idpatient);
    form.append('idpermission', idpermission);
    form.append('signedfileurl', signedfileurl);

    console.log("signedfileurl="+signedfileurl);

 
    return this.http.post<any>(ENV.url.relatives + '/signPermission', form);
  }
  
}
