import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';
import { FrontValuesService } from 'src/app/core/services/front-values.service';
import { LocalStorageService } from 'src/app/core/services/local-storage.service';

@Component({
  selector: 'app-request-password',
  templateUrl: './request-password.component.html',
  styleUrls: ['./request-password.component.scss']
})
export class RequestPasswordComponent implements OnInit {
  showMessagesSuccess: string;
  showMessagesError: string;
  email_username;
  submitted = true;
  captchaKey: string;
  captcha: string;


  constructor(
    private authenticationService: AuthService,
    private localStorageService: LocalStorageService,
    private frontValuesService: FrontValuesService,
  )
  {
    this.captchaKey = this.localStorageService.getObject('google.recaptcha.apikey');
  }

  ngOnInit(): void {
    this.getValuesConfig();
    this.captchaKey = this.localStorageService.getObject('google.recaptcha.apikey');
  }

  getValuesConfig() {
    this.frontValuesService.getFrontEndValuesCall().subscribe(
      result => {
        for (const item of result) {
          this.localStorageService.setObject(item.key, item.value);
        }
      }
    );
}

  requestPass() 
  {
    this.showMessagesSuccess = '';
    this.showMessagesError = '';
    this.submitted = false;

    let username = '';
    let email = '';

    if (this.email_username.includes('@')) 
    {
      username = '';
      email = this.email_username;
    } 
    else 
    {
      username = this.email_username;
      email = '';
    }

    this.authenticationService.requestRememberPassword(email, username, this.captcha).subscribe(
      result => {
        this.submitted = true;
        this.showMessagesSuccess = 'Se ha solicitado el cambio de contraseña. En breve le llegará un email para confirmar este cambio.';
        this.showMessagesError = '';
      },
      error => 
      {
        this.submitted = true;
        if (error.status === 409) 
        {
          this.showMessagesError = 'El captcha no se ha validado correctamente.';
          this.showMessagesSuccess = '';
        } 
        else 
        {
          this.showMessagesError = 'No se puede contactar con el servidor. Inténtalo más tarde.';
          this.showMessagesSuccess = '';
        }
      }
    );
  }

}
