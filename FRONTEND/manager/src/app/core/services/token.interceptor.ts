import { throwError as observableThrowError, Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import {
    HttpRequest,
    HttpHandler,
    HttpEvent,
    HttpInterceptor,
    HttpResponse,
    HttpErrorResponse
} from '@angular/common/http';
import { AuthService } from './auth.service'; 

import { map, catchError } from 'rxjs/operators';
import { Router } from '@angular/router';


@Injectable()
export class TokenInterceptor implements HttpInterceptor {

    constructor(
        public auth: AuthService,
        private router: Router
    ) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (this.auth.isAuthenticated()) {
            request = request.clone({
                setHeaders: {
                    Authorization: `Bearer ${this.auth.getToken()}`
                }
            });
        }
        return next.handle(request).pipe(map((event: HttpEvent<any>) => {
            /*if (event instanceof HttpResponse) {
              console.log('HttpResponse::event =', event, ';');
            } else{
                 console.log('event =', event, ';');
            }*/
            return event;
        }), catchError((err: any, _caught) => 
        {
            if (err instanceof HttpErrorResponse) 
            {
                if (err.status === 500) {
                    console.log('error 500');
                } else if (err.status === 504) {
                    console.log('error 504');
                } else if (err.status === 403) {
                    this.router.navigate(['/']);
                }
                return observableThrowError(err);
            }
        }));
    }
}
