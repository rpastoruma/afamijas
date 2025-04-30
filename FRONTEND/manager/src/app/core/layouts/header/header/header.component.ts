import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NbMenuService, NbSidebarService } from '@nebular/theme';
import { filter, map } from 'rxjs';
//import { RoleTranslate } from 'src/app/shared/models/models';
import { AuthService } from 'src/app/core/services/auth.service'; 

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  items: any[] = [];
  user: any;

  constructor(
    private sidebarService: NbSidebarService,
    private router: Router,
    private menuService: NbMenuService,
    private authenticationService: AuthService,
    ) { }

  ngOnInit(): void {
    this.setUser();
  }

  setUser() {
    this.items = [{ title: 'Cambiar contraseña'},{ title: 'Cerrar sesión'}];
    this.user = {
      id: this.authenticationService.getUserId(),
      name: this.cropFullname(this.authenticationService.getFullname()),
      roles: this.authenticationService.getRoles(), 
      documentid:  this.authenticationService.getDocumentid(),
      photo_url:  this.authenticationService.getPhotoURL(),
    };

   // console.log(JSON.stringify(this.user));

    this.menuService.onItemClick()
      .pipe(
        filter(({ tag }) => tag === 'my-context-menu'),
        map(({ item: { title } }) => title),
      )
      .subscribe(
        title => {
          if (title === 'Cerrar sesión') {
            this.logout();
          }
          else if (title === 'Cambiar contraseña') {
            this.goToChangePass();
          }
        }
      );
  } 


  cropFullname(fullname : string)
  {
    if(window.innerWidth > 750) return fullname;
    
    fullname = fullname.trim();

    if(fullname.length<=15) return fullname;

    let cropped = fullname.substring(0, 15);
    if(fullname.charAt(14) == ' ' || fullname.charAt(15) == ' ') return cropped;

    else return cropped + "…";
  }

  toggleSidebar(): boolean {
    this.sidebarService.toggle(true, 'menu-sidebar');
    return false;
  }

  goToHome() {
    this.router.navigate(['/login']);
  }

  goToChangePass() {
    this.router.navigate(['/change-pass']);
  }


  logout() {
    this.router.navigate(['/login']);
  }
}
