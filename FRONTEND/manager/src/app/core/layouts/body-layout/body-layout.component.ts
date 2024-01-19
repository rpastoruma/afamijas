import { Component, OnInit } from '@angular/core';
import { hasRole, RoleCode, } from 'src/app/shared/models/models';
import { AuthService } from '../../services/auth.service'; 
import { MENU_ITEMS, MENU_RELATIVE_ITEMS } from './pages-menu';

@Component({
  selector: 'app-body-layout',
  templateUrl: './body-layout.component.html',
  styleUrls: ['./body-layout.component.scss']
})
export class BodyLayoutComponent implements OnInit {

  menu = MENU_ITEMS;

  constructor(private authenticationService: AuthService) { }

  ngOnInit(): void {
    this.setMenuData();
  }

  setMenuData() 
  {
    const role = this.authenticationService.getRole();
    if (role) 
    {
      if (hasRole(role, RoleCode.RELATIVE) || hasRole(role, RoleCode.ROOT) ) 
      {
        this.menu = this.menu.concat(MENU_RELATIVE_ITEMS);
      } 

    }
    
  }
}
