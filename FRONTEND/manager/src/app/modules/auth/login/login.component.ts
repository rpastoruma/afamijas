import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';
import { DataStorageService } from 'src/app/core/services/data-storage.service';
import { FrontValuesService } from 'src/app/core/services/front-values.service';
import { LocalStorageService } from 'src/app/core/services/local-storage.service';



@Component({
  selector: 'ngx-login',
  templateUrl: './login.component.html',
})


export class LoginComponent implements OnInit
{

  constructor(
    private localStorageService: LocalStorageService,
    private router: Router,
    private authService: AuthService,
    private frontValuesService: FrontValuesService,
    private dataStorage: DataStorageService
  ) {}


  username = '';
  password = '';
  submitted = false;
  errorUsername = false;
  errorPassword = false;
  messageError = '';
  messageOk = '';


  ngOnInit(): void 
  {
    this.authService.logout();
    this.dataStorage.setUserInfo(undefined);
    this.getValuesConfig();
  } 


  login() 
  {
    this.authService.login(this.username, this.password).subscribe(
      _ => 
      {
        // login successful
        this.messageError = '';
        this.messageOk = '¡Has conectado con éxito!';
        this.submitted = true;

        if(this.authService.isAuthenticated()) this.router.navigate(['/calendar']);
      }, 
      error => 
      {
        this.messageOk = '';
        this.submitted = false;
        if (error.status === 403) 
          this.messageError = 'Usuario o contraseña no válidos.';
        else 
          this.messageError = 'No se puede contactar con el servidor. Inténtalo más tarde.';
      });
  }

  getValuesConfig() 
  {
      this.frontValuesService.getFrontEndValuesCall().subscribe(
        result => {
          for (const item of result) {
            this.localStorageService.setObject(item.key, item.value);
          }
        }
      );
  }
  
  validate() 
  {
    if (!this.username && !this.password) 
    {
      this.errorUsername = true;
      this.errorPassword = true;
      return false;
    } 
    else if (!this.username) 
    {
      this.errorUsername = true;
      this.errorPassword = false;
      return false;
    } 
    else if (!this.password) 
    {
      this.errorUsername = false;
      this.errorPassword = true;
      return false;
    } 
    else 
    {
      this.errorUsername = false;
      this.errorPassword = false;
      return true;
    }
  }




}