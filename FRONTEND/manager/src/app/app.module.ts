import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CoreModule } from './core/core.module';
import { HttpClientModule } from '@angular/common/http';
import { NbAuthModule } from '@nebular/auth';
import { NbIconLibraries } from '@nebular/theme';
import { NbEvaIconsModule } from '@nebular/eva-icons';
import { NbIconModule } from '@nebular/theme';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NbToastrModule } from '@nebular/theme';

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    AppRoutingModule,
    HttpClientModule,
    CoreModule, // core
    NbAuthModule.forRoot({}),
    NbEvaIconsModule,
    NbIconModule,
    NgbModule,
    NbToastrModule.forRoot()
    ],
  providers: [
  ],
  bootstrap: [AppComponent]
})
export class AppModule
 {

  constructor(private iconLibraries: NbIconLibraries) {
    this.iconLibraries.registerFontPack('fa', { packClass: 'fa' });
    this.iconLibraries.registerFontPack('fas', { packClass: 'fas' });
    this.iconLibraries.registerFontPack('far', { packClass: 'far' });
  }

  }
