import { throwError as observableThrowError, Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { map, catchError } from 'rxjs/operators';

import { environment as ENV } from '../../environments/environment'; 
import { LocalStorageService } from './local-storage.service';
import { LoginResponse } from 'src/app/shared/models/models';

@Injectable()
export class AuthService {

  constructor(
    private http: HttpClient,
    private localStorageService: LocalStorageService
  ) 
  {
  }

  public isAuthenticated(): boolean {
    // get the token
    const token = this.getToken();
    if (token === null) {
      return false;
    }
    return true;
  }

  
  getToken(): string | null {
    const value = this.localStorageService.getObject('logged');
    if (value && value.token) {
      return value.token;
    }
    return null;
  }

  getUserId(): string {
    const data: LoginResponse = this.localStorageService.getObject('logged');
    return data.userId;
  }

  getRole() {
    const data: LoginResponse = this.localStorageService.getObject('logged');
    return data.role;
  }

  getUsername() {
    const data: LoginResponse = this.localStorageService.getObject('logged');
    return data.username;
  }

  getDni() {
    const data: LoginResponse = this.localStorageService.getObject('logged');
    return data.dni;
  }

  getFullname() {
    const data: LoginResponse = this.localStorageService.getObject('logged');
    return data.fullname;
  }

  getPhotoURL() {
    const data: LoginResponse = this.localStorageService.getObject('logged');
    return data.photo_url==null?'/assets/generic-user-avatar.jpg':data.photo_url;
  }

  isRoot()
  {
    return this.getRole() === 'ROOT';
  }


  isRelative()
  {
    return this.getRole() === 'RELATIVE';
  }



  login(username: string, password: string): Observable<string | null> 
  {
    // this.logout();

    const form = new FormData();
    form.append('username', username);
    form.append('password', password);

    return this.http.post<LoginResponse>(ENV.url.login, form).pipe(map((data: LoginResponse) => 
    {
        // login successful if there's a jwt token in the response
        try 
        {
          if (data.token && data.role) 
          {
            // store username and jwt token in local storage to keep user logged in between page refreshes
            this.localStorageService.setObject('logged', data);
            // return true to indicate successful login
            return data.userId;
          } 
          else 
          {
            // return false to indicate failed login
            return null
          }
        } 
        catch (error) 
        {
          return null;
        }
      }), 
      catchError((error: any) => observableThrowError(error || 'Server error')));
  }

  logout(): void 
  {
    // clear token remove user from local storage to log user out
    this.localStorageService.clear();
  }


  
  requestRememberPassword(email: string, username: string, captchatoken: string) 
  {
    const form = new FormData();
    if (email) { form.append('email', email); }
    if (username) { form.append('username', username); }
    if (captchatoken) { form.append('captchatoken', captchatoken); }

    return this.http.post<any>(ENV.url.auth + `requestRememberPassword`, form, {responseType: 'text' as 'json'});
  }


  /*

  b64EncodeUnicode(str: string): string {
    return btoa(encodeURIComponent(str).replace(/%([0-9A-F]{2})/g, (match, p1) => {
      return String.fromCharCode(('0x' + p1) as any);
    }));
  }

    isExpired(exp: any): boolean {
    if (+exp) {
      return new Date().getTime() > (+exp * 1000);
    }
    return false;
  }

*/

}