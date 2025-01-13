import { Component, HostListener } from '@angular/core';
import { LocalStorageService } from './core/services/local-storage.service'; 


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  @HostListener('window:beforeunload', ['$event'])
  unloadNotification($event: any): void {
    const confirmationMessage = '¿Estás seguro de que quieres salir?';
    $event.returnValue = confirmationMessage; // Esto mostrará la ventana de confirmación del navegador
  }

  
  title = 'manager';

  constructor(
    localStorageService: LocalStorageService
  ) {
  }

}
