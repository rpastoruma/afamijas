import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UsersService } from 'src/app/core/services/users.service';


@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit
{

  constructor(
    private router: Router,
    private usersService: UsersService
  ) {}


  password = '';
  newpassword = '';
  submitted = false;
  messageError = '';
  messageOk = '';


  ngOnInit(): void 
  {
  } 


  changePass() {
    this.messageError = '';
    this.submitted = true;
  
    this.usersService.changePass(this.password, this.newpassword).subscribe(
      response => {
        this.messageError = '';
        this.messageOk = '';
        this.submitted = true;
  
        if (!response) {
          this.messageError = 'Respuesta inesperada del servidor.';
          return;
        }
  
        this.messageOk = 'La contraseña se ha cambiado correctamente.';

        setTimeout(() => {
          console.log('Redirigiendo a /calendar con token: ', localStorage.getItem('logged'));
this.router.navigate(['/calendar']);
        }
        , 2000);
        
      },  
      error => {
        this.messageOk = '';
        this.submitted = false;

        
        if (error.status === 409) {
          this.messageError = 'Las contraseñas no coinciden.';
        } else if (error.status === 406) {
          this.messageError = 'La nueva contraseña debe tener al menos 9 caracteres y contener letras mayúsculas, minúsculas, números y al menos uno de los siguientes símbolos: % _ $ - ! *';
        } else {
          this.messageError = 'No se puede contactar con el servidor. Inténtalo más tarde.';
        }
      }
    );
  }
  

  isPasswordValid(): boolean {
    const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[%_\$\-\!\*])[A-Za-z\d%_\$\-\!\*]{9,}$/;
    return regex.test(this.password);
  }
  
}