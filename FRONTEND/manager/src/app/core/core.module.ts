import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CoreRoutingModule } from './core-routing.module';
import { NbThemeModule, NbLayoutModule, NbCardModule, NbSidebarModule,
  NbUserModule, NbContextMenuModule, NbMenuModule, NbListModule, NbBadgeModule, NbTooltipModule, NbIconModule } from '@nebular/theme';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SharedModule } from '../shared/shared.module';
import { AuthService } from './services/auth.service';
import { LocalStorageService } from './services/local-storage.service';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { TokenInterceptor } from './services/token.interceptor';
//import { NotificationsComponent } from './layouts/notifications/notifications.component';
import { MomentModule } from 'ngx-moment';
import { HeaderComponent } from './layouts/header/header/header.component';
import { BodyLayoutComponent } from './layouts/body-layout/body-layout.component';
import { NotificationsComponent } from './layouts/notifications/notifications.component';


@NgModule({
    imports: [
      CommonModule,
      CoreRoutingModule,
      BrowserModule,
      BrowserAnimationsModule,
      NbThemeModule.forRoot({ name: 'default' }),
      NbLayoutModule,
      SharedModule,
      NbCardModule,
      NbUserModule,
      NbContextMenuModule,
      NbMenuModule,
      NbSidebarModule,
      NbListModule,
      MomentModule,
      NbBadgeModule,
      NbTooltipModule,
      NbIconModule,
          ],
    exports: [
      CommonModule,
      NbCardModule,
      NbIconModule
    ],
    providers: [
      AuthService,
      LocalStorageService,
      NbSidebarModule.forRoot().providers,
      NbMenuModule.forRoot().providers,
      {
        provide: HTTP_INTERCEPTORS,
        useClass: TokenInterceptor,
        multi: true
      }
    ],
    declarations: [
      HeaderComponent,
      BodyLayoutComponent,
      NotificationsComponent
    ]
})
export class CoreModule { }
