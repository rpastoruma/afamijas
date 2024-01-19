import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NbMenuService, NbSidebarService } from '@nebular/theme';
import { filter, map } from 'rxjs';
import { RoleTranslate } from 'src/app/shared/models/models';
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
    this.items = [{ title: 'Cerrar sesión'}];
    this.user = {
      id: this.authenticationService.getUserId(),
      name: this.authenticationService.getFullname(),
      role: this.authenticationService.getRole() ? RoleTranslate[this.authenticationService.getRole()] : '',
      dni:  this.authenticationService.getDni(),
      photo_url:  this.authenticationService.getPhotoURL(),
    };

    console.log(JSON.stringify(this.user));

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
        }
      );
  }



  toggleSidebar(): boolean {
    this.sidebarService.toggle(true, 'menu-sidebar');
    return false;
  }

  goToHome() {
    this.router.navigate(['/login']);
  }

  logout() {
    this.router.navigate(['/login']);
  }
}
