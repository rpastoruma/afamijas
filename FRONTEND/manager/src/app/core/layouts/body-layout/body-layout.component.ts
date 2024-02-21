import { Component, OnInit } from '@angular/core';
import { hasRole, RoleCode, } from 'src/app/shared/models/models';
import { AuthService } from '../../services/auth.service'; 
import { MENU_FOODS, MENU_ITEMS, MENU_MEDICATION, MENU_RELATIVE_ITEMS } from './pages-menu';

@Component({
  selector: 'app-body-layout',
  templateUrl: './body-layout.component.html',
  styleUrls: ['./body-layout.component.scss']
})
export class BodyLayoutComponent implements OnInit {

  menu = MENU_ITEMS; // MENÚ GENERAL PARA TODOS LOS USUARIOS (CALENDARIO, ...)
  isMobileLayout : boolean = false;

  constructor(private authenticationService: AuthService) { }

  ngOnInit(): void {
    this.setMenuData();
    this.isMobileLayout = window.innerWidth <= 750;
    window.onresize = () => this.isMobileLayout = window.innerWidth <= 750;
  }

  setMenuData() 
  {
    const roles = this.authenticationService.getRoles();
    if (roles) 
    {
      if (hasRole(roles, RoleCode.RELATIVE) ) 
      {
        this.menu = this.menu.concat(MENU_RELATIVE_ITEMS);
      } 

      if (hasRole(roles, RoleCode.NURSING) || hasRole(roles, RoleCode.NURSING_ASSISTANT)  || hasRole(roles, RoleCode.ADMIN)  || hasRole(roles, RoleCode.MANAGER)
          || hasRole(roles, RoleCode.PSYCHOLOGIST)  || hasRole(roles, RoleCode.SOCIAL_WORKER)  || hasRole(roles, RoleCode.PHYSIOTHERAPIST)  || hasRole(roles, RoleCode.OCCUPATIONAL_THERAPIST) 
          || hasRole(roles, RoleCode.OPERATOR_EXTRA_1)
       ) 
      {
        this.menu = this.menu.concat(MENU_MEDICATION);
      } 


      if (hasRole(roles, RoleCode.KITCHEN) || hasRole(roles, RoleCode.ADMIN)  || hasRole(roles, RoleCode.MANAGER)
       ) 
      {
        this.menu = this.menu.concat(MENU_FOODS);
      } 




    }
    
  }
}
