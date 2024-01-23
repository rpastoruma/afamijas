import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
/*
import { CanAccessAcademicTitle } from './guards/academic-title/canAccessAcademicTitle';
import { CanAccessCenter } from './guards/center/canAccessCenter';
import { CanAccessConfiguration } from './guards/configuration/canAccessConfiguration';
import { CanAccessCriteria } from './guards/criteria/canAccessCriteria';
import { CanAccessEditorial } from './guards/editorial/canAccessEditorial';
import { HasLogin } from './guards/has-login';
import { CanAccessKeyword } from './guards/keyword/canAccessKewyword';
import { CanAccessLog } from './guards/log/canAccessLog';
import { CanAccessMasterTable } from './guards/master-table/canAccessMasterTable';
import { CanAccessOffer } from './guards/offer/canAccessOffer';
import { CanAccessPreresearcher } from './guards/preresearcher/canAccessPreresearcher';
import { CanAccessReport } from './guards/report/canAccessReport';
import { CanAccessResearchGroup } from './guards/research-group/canAccessResearchGroup';
import { CanAccessResearchLine } from './guards/research-line/canAccessResearchLine';
import { CanAccessResearcher } from './guards/researcher/canAccessResearcher';
import { CanAccessScientificProduction } from './guards/scientific-production/canAccessScientificProduction';
import { CanAccessUniversity } from './guards/university/canAccessUniversity';
import { CanAccessUser } from './guards/user/canAccessUser';
*/
import { BodyLayoutComponent } from './layouts/body-layout/body-layout.component'; 
import { canViewCalendarGuard } from './guards/can-view-calendar.guard';
import { canViewAbsencesGuard } from './guards/can-view-absences.guard';

const routes: Routes = [
    /*
    {
      path: 'configuration',
      //canActivate: [CanAccessConfiguration],
      component: UserLayoutComponent,
      loadChildren: () => import('../modules/configuration/configuration.module').then(m => m.ConfigurationModule)
    },
    {
      path: 'profile',
      //canActivate: [HasLogin],
      component: UserLayoutComponent,
      loadChildren: () => import('../modules/user-profile/user-profile.module').then(m => m.UserProfileModule)
    },
    {
      path: 'logs',
      //canActivate: [CanAccessLog],
      component: UserLayoutComponent,
      loadChildren: () => import('../modules/logs/logs.module').then(m => m.LogsModule)
    },
    {
      path: 'users',
      //canActivate: [CanAccessUser],
      component: UserLayoutComponent,
      loadChildren: () => import('../modules/users/user.module').then(m => m.UserModule)
    }, */ 
    {
      path : 'calendar', 
      component: BodyLayoutComponent,
      canActivate : [canViewCalendarGuard],
      loadChildren: () => import('../modules/calendar/mycalendar.module').then(m => m.MyCalendarModule) 
    },    
    {
      path : 'absences', 
      component: BodyLayoutComponent,
      canActivate : [canViewAbsencesGuard],
      loadChildren: () => import('../modules/absences/absences.module').then(m => m.AbsencesModule) 
    },
    {
      // login or register (must be after others routes)
      path: '',
      loadChildren: () => import('../modules/auth/auth.module').then(m => m.AuthModule)
    },
    // other routes (must be the last)
    { path: '**', redirectTo: 'login' },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
    /*
    providers: [CanAccessConfiguration, CanAccessScientificProduction, CanAccessResearchLine, CanAccessAcademicTitle, CanAccessResearchGroup,
      CanAccessResearcher, CanAccessMasterTable, CanAccessCenter, CanAccessUniversity, CanAccessKeyword, HasLogin, CanAccessLog,
      CanAccessUser, CanAccessPreresearcher, CanAccessCriteria, CanAccessEditorial, CanAccessOffer, CanAccessReport] */
})
export class CoreRoutingModule { }
