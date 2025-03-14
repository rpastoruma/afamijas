import { Component, OnInit } from '@angular/core';
import { hasRole, RoleCode, } from 'src/app/shared/models/models';
import { AuthService } from '../../services/auth.service'; 
import { MENU_CLEANING, MENU_DOCS, MENU_FEEDINGS, MENU_FOODS, MENU_HEALTH, MENU_INVOICES, MENU_ITEMS, MENU_LEGIONELLA, MENU_MEDICATION, MENU_MEMBERS, MENU_PATIENTS, MENU_RECEIPTS, MENU_RELATIVE_ITEMS, MENU_TEMP, MENU_PSICO, MENU_SOCIAL_WORKER, MENU_ATENCIONES, MENU_FISIO } from './pages-menu';

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

      if (hasRole(roles, RoleCode.ADMIN) || hasRole(roles, RoleCode.MANAGER) ) 
      {
        this.menu = this.menu.concat(MENU_PATIENTS);
      } 


      if (hasRole(roles, RoleCode.RELATIVE) ) 
      {
        this.menu = this.menu.concat(MENU_RELATIVE_ITEMS);
      } 

      if (hasRole(roles, RoleCode.NURSING) || hasRole(roles, RoleCode.NURSING_ASSISTANT)  || hasRole(roles, RoleCode.ADMIN)  || hasRole(roles, RoleCode.MANAGER)
          || hasRole(roles, RoleCode.PSYCHOLOGIST)  || hasRole(roles, RoleCode.SOCIAL_WORKER)  || hasRole(roles, RoleCode.PHYSIOTHERAPIST)  || hasRole(roles, RoleCode.OCCUPATIONAL_THERAPIST) 
          || hasRole(roles, RoleCode.OPERATOR_EXTRA_1)       ) 
      {
        this.menu = this.menu.concat(MENU_MEDICATION);
      } 

      if (hasRole(roles, RoleCode.KITCHEN) || hasRole(roles, RoleCode.ADMIN)  || hasRole(roles, RoleCode.MANAGER)      ) 
      {
        this.menu = this.menu.concat(MENU_FOODS);
      } 

      if (hasRole(roles, RoleCode.NURSING_ASSISTANT) || hasRole(roles, RoleCode.ADMIN)  || hasRole(roles, RoleCode.MANAGER)      ) 
      {
        this.menu = this.menu.concat(MENU_FEEDINGS);
      } 

      if (hasRole(roles, RoleCode.KITCHEN) || hasRole(roles, RoleCode.ADMIN)  || hasRole(roles, RoleCode.MANAGER)       ) 
      {
        this.menu = this.menu.concat(MENU_TEMP);
      } 

      if (hasRole(roles, RoleCode.LEGIONELLA_CONTROL) || hasRole(roles, RoleCode.ADMIN)  || hasRole(roles, RoleCode.MANAGER)       ) 
      {
        this.menu = this.menu.concat(MENU_LEGIONELLA);
      } 

      if (hasRole(roles, RoleCode.CLEANING) || hasRole(roles, RoleCode.ADMIN)  || hasRole(roles, RoleCode.MANAGER)      ) 
      {
        this.menu = this.menu.concat(MENU_CLEANING);
      } 

      if (hasRole(roles, RoleCode.NURSING) || hasRole(roles, RoleCode.NURSING_ASSISTANT) || hasRole(roles, RoleCode.ADMIN)  || hasRole(roles, RoleCode.MANAGER)      ) 
      {
        this.menu = this.menu.concat(MENU_HEALTH);
      } 

      if (hasRole(roles, RoleCode.PSYCHOLOGIST) ) 
      {
          this.menu = this.menu.concat(MENU_PSICO);
      } 
  

      if (hasRole(roles, RoleCode.SOCIAL_WORKER) ) 
      {
        this.menu = this.menu.concat(MENU_SOCIAL_WORKER);
        this.menu = this.menu.concat(MENU_ATENCIONES);
      } 


      if (hasRole(roles, RoleCode.PHYSIOTHERAPIST) ) 
      {
          this.menu = this.menu.concat(MENU_FISIO);
      } 
      
    

      if (hasRole(roles, RoleCode.ADMIN) || hasRole(roles, RoleCode.MANAGER) ) 
      {
        this.menu = this.menu.concat(MENU_MEMBERS);
        this.menu = this.menu.concat(MENU_RECEIPTS);
        this.menu = this.menu.concat(MENU_INVOICES);
        this.menu = this.menu.concat(MENU_PSICO);
        this.menu = this.menu.concat(MENU_SOCIAL_WORKER);
        this.menu = this.menu.concat(MENU_ATENCIONES);
        this.menu = this.menu.concat(MENU_FISIO); //TODO: QUITAR
      } 
  


      //PARA TODOS LOS USUARIOS:
      this.menu = this.menu.concat(MENU_DOCS);



    }
    
  }
}
