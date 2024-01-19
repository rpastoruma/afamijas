import { Component } from '@angular/core';
import { LocalStorageService } from './core/services/local-storage.service'; 


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'manager';

  constructor(
    localStorageService: LocalStorageService
  ) {
  }

}
