import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LocalStorageService } from './local-storage.service';
import { environment as ENV} from '../../environments/environment'; 

@Injectable({
  providedIn: 'root'
})

export class FrontValuesService 
{
  parameters: any;

  constructor(private http: HttpClient, private localStorage: LocalStorageService) { }

  doGetFrontValues() {
    return this.http.get<any>(ENV.url.config + '/getFrontEndValues');
  }

  getFrontEndValues() {
    this.doGetFrontValues().subscribe(
      result => {
        this.parameters = result;
        for (const item of this.parameters) {
          this.localStorage.setObject(item.key, item.value);
        }
      },
      error => {
        this.parameters = null;
      }
    );
  }

  getFrontEndValuesCall() {
    return this.doGetFrontValues();
  }

  getAllValues() {
    return this.parameters;
  }

  getValue(key: string) {
    const item = this.parameters ? this.parameters.find(elem => elem.key === key) : null;
    if (item) {
      return item.value;
    }
    return '';
  }
}
