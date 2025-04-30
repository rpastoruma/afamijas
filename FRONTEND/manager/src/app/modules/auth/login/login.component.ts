import { Component, HostListener, Inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';
import { DataStorageService } from 'src/app/core/services/data-storage.service';
import { FrontValuesService } from 'src/app/core/services/front-values.service';
import { LocalStorageService } from 'src/app/core/services/local-storage.service';
import { DOCUMENT } from '@angular/common';


@Component({
  selector: 'ngx-login',
  templateUrl: './login.component.html',
})


export class LoginComponent implements OnInit
{
  elem: any;
  isFullScreen: boolean;


  showQR: boolean = false;
  qrUrl: string = '';
  showCodeInput: boolean = false;
  code2FA: string = '';


  constructor(
    private localStorageService: LocalStorageService,
    private router: Router,
    private authService: AuthService,
    private frontValuesService: FrontValuesService,
    private dataStorage: DataStorageService,
    @Inject(DOCUMENT) private document: any
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

    //FULL SCREEN
    //this.chkScreenMode();
    //this.elem = document.documentElement;
  } 

  /* FULL SCREEN

  @HostListener('document:fullscreenchange', ['$event'])
  @HostListener('document:webkitfullscreenchange', ['$event'])
  @HostListener('document:mozfullscreenchange', ['$event'])
  @HostListener('document:MSFullscreenChange', ['$event'])

  fullscreenmodes(event){
    this.chkScreenMode();
  }

  chkScreenMode(){
    if(document.fullscreenElement){
      //fullscreen
      this.isFullScreen = true;
    }else{
      //not in full screen
      this.isFullScreen = false;
    }
  }

  openFullscreen() {
    if (this.elem.requestFullscreen) {
      this.elem.requestFullscreen();
    } else if (this.elem.mozRequestFullScreen) {
      // Firefox 
      this.elem.mozRequestFullScreen();
    } else if (this.elem.webkitRequestFullscreen) {
      // Chrome, Safari and Opera
      this.elem.webkitRequestFullscreen();
    } else if (this.elem.msRequestFullscreen) {
      // IE/Edge 
      this.elem.msRequestFullscreen();
    }
  }

  closeFullscreen() {
    if (this.document.exitFullscreen) {
      this.document.exitFullscreen();
    } else if (this.document.mozCancelFullScreen) {
      // Firefox 
      this.document.mozCancelFullScreen();
    } else if (this.document.webkitExitFullscreen) {
      // Chrome, Safari and Opera 
      this.document.webkitExitFullscreen();
    } else if (this.document.msExitFullscreen) {
      // IE/Edge 
      this.document.msExitFullscreen();
    }
  }

  */

  login1() {
    this.messageError = '';
    this.messageOk = '';
    this.submitted = true;
  
    this.authService.login1(this.username, this.password).subscribe(
      response => {
        this.messageError = '';
        this.messageOk = '';
        this.submitted = true;
  
        if (!response) {
          this.messageError = 'Respuesta inesperada del servidor.';
          return;
        }

        if (response && response.token) {
          this.authService.saveLogin(response); // Guarda token y usuario en localStorage, etc.
          if(response.passworChanged && response.passworChanged == true)
            this.router.navigate(['/calendar']);
          else
            this.router.navigate(['/change-pass']);
          return;
        }
  
        // Si el backend ha devuelto un URL para el QR, lo mostramos
        if (response.otpAuthUrl) {
          this.showQRCode(response.otpAuthUrl); // Muestra el QR
          this.messageOk = 'Escanea el QR con Google Authenticator y luego introduce el código 2FA.';
          return;
        }
  
        // Si requiere 2FA, solo mostrar el campo para ingresar el código
        if (response.requires2FA) {
          this.showCodeInput = true;
          this.messageOk = response.message || 'Introduce el código de autenticación.';
          return;
        }
  
        this.messageError = 'No se pudo iniciar sesión. Inténtalo de nuevo.';
      },
      error => {
        this.messageOk = '';
        this.submitted = false;
        if (error.status === 403) {
          this.messageError = 'Usuario o contraseña no válidos.';
        } else {
          this.messageError = 'No se puede contactar con el servidor. Inténtalo más tarde.';
        }
      }
    );
  }
  

  showQRCode(otpAuthUrl: string) {
    // Usamos qrserver.com para generar el QR con la URL de autenticación de Google Authenticator
    this.qrUrl = `https://api.qrserver.com/v1/create-qr-code/?data=${encodeURIComponent(otpAuthUrl)}&size=200x200`;
    this.showQR = true;  // Esto se utiliza en el HTML para mostrar el QR
  }
  
  

  login2() {
    if (!this.username || !this.code2FA) {
      this.messageError = 'Debes introducir el código de autenticación.';
      return;
    }
  
    this.authService.login2(this.username, this.code2FA).subscribe(
      response => {
        this.messageError = '';
        this.messageOk = '¡Autenticación completada con éxito!';
        this.submitted = true;
  
        if (response && response.token) {
          this.authService.saveLogin(response); // Guarda token y usuario en localStorage, etc.
          if(response.passworChanged && response.passworChanged == true)
            this.router.navigate(['/calendar']);
          else
            this.router.navigate(['/change-pass']);
        } else {
          this.messageError = 'No se pudo completar el inicio de sesión.';
          this.submitted = false;
        }
      },
      error => {
        this.messageOk = '';
        this.submitted = false;
        if (error.status === 401) {
          this.messageError = 'Código de autenticación incorrecto.';
        } else {
          this.messageError = 'No se puede verificar el código. Inténtalo más tarde.';
        }
      }
    );
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