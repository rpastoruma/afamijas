import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 


@Injectable({
  providedIn: 'root'
})
export class MediaService {

  constructor(private http: HttpClient,) { }


  uploadFile(file : File) 
  {
    const form = new FormData();
    form.append('file', file);
 
    return this.http.post<any>(ENV.url.media + '/uploadFile', form);
  }



}
