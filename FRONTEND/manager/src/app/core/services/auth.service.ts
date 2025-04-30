import { throwError as observableThrowError, Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { map, catchError } from 'rxjs/operators';

import { environment as ENV } from '../../environments/environment'; 
import { LocalStorageService } from './local-storage.service';
import { LoginResponse } from 'src/app/shared/models/models';
import { hasRole, RoleCode } from 'src/app/shared/models/models';

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

  getRoles() {
    const data: LoginResponse = this.localStorageService.getObject('logged');
    return data.roles;
  }

  getUsername() {
    const data: LoginResponse = this.localStorageService.getObject('logged');
    return data.username;
  }

  getDocumentid() {
    const data: LoginResponse = this.localStorageService.getObject('logged');
    return data.documentid;
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
    return hasRole(this.getRoles(),  RoleCode.ROOT);
  }


  isRelative()
  {
    return hasRole(this.getRoles(),  RoleCode.RELATIVE);
  }


  isAdmin()
  {
    return hasRole(this.getRoles(),  RoleCode.ADMIN);
  }

  isManager()
  {
    return hasRole(this.getRoles(),  RoleCode.MANAGER);
  }

  
  isNursingAssitant()
  {
    return hasRole(this.getRoles(),  RoleCode.NURSING_ASSISTANT);
  }

  
  isNursing()
  {
    return hasRole(this.getRoles(),  RoleCode.NURSING);
  }


    
  isKitchen()
  {
    return hasRole(this.getRoles(),  RoleCode.KITCHEN);
  }


    
  isLegionellaLog()
  {
    return hasRole(this.getRoles(),  RoleCode.LEGIONELLA_CONTROL);
  }


  isCleaning()
  {
    return hasRole(this.getRoles(),  RoleCode.CLEANING);
  }

  isWorker()
  {
    return this.isAdmin() || this.isManager() || this.isNursing() || this.isNursingAssitant() || this.isKitchen() || this.isLegionellaLog() || this.isCleaning();
  }



  login1(username: string, password: string): Observable<LoginResponse | null> {
    const form = new FormData();
    form.append('username', username);
    form.append('password', password);
  
    return this.http.post<LoginResponse>(ENV.url.login1, form).pipe(
      map((data: LoginResponse) => {
        // Siempre esperamos requires2FA = true, y opcionalmente otpAuthUrl o message
        if (data.requires2FA) {
          return data;
        }

        //si devuelve token (porque se haya autentificado en menos de 1 día)
        if (data.token) {
          return data;
        }
  
        // Si no se requiere 2FA ni se devuelve mensaje útil, es una respuesta inesperada
        return null;
      }),
      catchError((error: any) => observableThrowError(error || 'Server error'))
    );
  }


  login2(username: string, code2FA: string): Observable<LoginResponse> {
    const form = new FormData();
    form.append('username', username);
    form.append('code2FA', code2FA);
  
    return this.http.post<LoginResponse>(ENV.url.login2, form);
  }
  
  saveLogin(response: LoginResponse): void {
    this.localStorageService.setObject('logged', response);
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
