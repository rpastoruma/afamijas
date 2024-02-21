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

  login() 
  {
    //FULL SCREEN
    //this.openFullscreen();
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