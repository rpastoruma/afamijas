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
import { canViewRouteGuard } from './guards/can-view-route.guard';
import { canViewMenuGuard } from './guards/can-view-menu.guard';
import { canViewPermissionsGuard } from './guards/can-view-permissions.guard';
import { canViewWorkerMedicationGuard } from './guards/can-view-worker-medication.guard';
import { canViewWorkerFoodGuard } from './guards/can-view-worker-food.guard';
import { canViewWorkerFeedingRegisterGuard } from './guards/can-view-worker-feeding-register.guard';
import { canViewWorkerTempRegisterGuard } from './guards/can-view-worker-temp-register.guard';
import { canViewLegionellalogGuard } from './guards/can-view-legionellalog.guard';
import { canViewWclogsGuard } from './guards/can-view-wclogs.guard';
import { canViewHealthLogsGuard } from './guards/can-view-health-logs.guard';
import { canViewDocsGuard } from './guards/can-view-docs.guard';
import { canAccessMembersGuard } from './guards/can-access-members.guard';
import { canAccessInvoicesGuard } from './guards/can-access-invoices.guard';
import { canViewDocsPsicoGuard } from './guards/can-view-docs-psico.guard';
import { canViewSocialWorkerGuard } from './guards/can-view-social-worker.guard';
import { canAccessAtencionesGuard } from './guards/can-access-atenciones.guard';
import { canViewFisioWorkerGuard } from './guards/can-view-fisio-worker.guard';


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
      path : 'routes', 
      component: BodyLayoutComponent,
      canActivate : [canViewRouteGuard],
      loadChildren: () => import('../modules/routes/routes.module').then(m => m.RoutesModule) 
    },    
    {
      path : 'absences', 
      component: BodyLayoutComponent,
      canActivate : [canViewAbsencesGuard],
      loadChildren: () => import('../modules/absences/absences.module').then(m => m.AbsencesModule) 
    },    
    {
      path : 'menus', 
      component: BodyLayoutComponent,
      canActivate : [canViewMenuGuard],
      loadChildren: () => import('../modules/menus/menus.module').then(m => m.MenusModule) 
    },    
    {
      path : 'permissions', 
      component: BodyLayoutComponent,
      canActivate : [canViewPermissionsGuard],
      loadChildren: () => import('../modules/permission/permission.module').then(m => m.PermissionModule) 
    },
    {
      path : 'worker-medication', 
      component: BodyLayoutComponent,
      canActivate : [canViewWorkerMedicationGuard],
      loadChildren: () => import('../modules/worker-medication/worker-medication.module').then(m => m.WorkerMedicationModule) 
    },
    {
      path : 'worker-food', 
      component: BodyLayoutComponent,
      canActivate : [canViewWorkerFoodGuard],
      loadChildren: () => import('../modules/worker-food/worker-food.module').then(m => m.WorkerFoodModule) 
    },
    {
      path : 'worker-feeding', 
      component: BodyLayoutComponent,
      canActivate : [canViewWorkerFeedingRegisterGuard],
      loadChildren: () => import('../modules/worker-feeding-register/worker-feeding-register.module').then(m => m.WorkerFeedingRegisterModule) 
    },
    {
      path : 'worker-temp', 
      component: BodyLayoutComponent,
      canActivate : [canViewWorkerTempRegisterGuard],
      loadChildren: () => import('../modules/worker-temp-register/worker-temp-register.module').then(m => m.WorkerTempRegisterModule) 
    },
    {
      path : 'worker-legionella', 
      component: BodyLayoutComponent,
      canActivate : [canViewLegionellalogGuard],
      loadChildren: () => import('../modules/worker-legionella/worker-legionella.module').then(m => m.WorkerLegionellaModule) 
    },
    {
      path : 'worker-wclogs', 
      component: BodyLayoutComponent,
      canActivate : [canViewWclogsGuard],
      loadChildren: () => import('../modules/worker-wclog/worker-wclog.module').then(m => m.WorkerWclogModule) 
    },
    {
      path : 'worker-healthlogs', 
      component: BodyLayoutComponent,
      canActivate : [canViewHealthLogsGuard],
      loadChildren: () => import('../modules/worker-health-log/worker-health-log.module').then(m => m.WorkerHealthLogModule) 
    },
    {
      path : 'worker-docs', 
      component: BodyLayoutComponent,
      canActivate : [canViewDocsGuard],
      loadChildren: () => import('../modules/worker-documents/worker-documents.module').then(m => m.WorkerDocumentsModule) 
    },
    {
      path : 'worker-docs-psico', 
      component: BodyLayoutComponent,
      canActivate : [canViewDocsPsicoGuard],
      loadChildren: () => import('../modules/worker-documents/worker-documents.module').then(m => m.WorkerDocumentsModule) 
    },
    {
      path : 'social-worker-docs', 
      component: BodyLayoutComponent,
      canActivate : [canViewSocialWorkerGuard],
      loadChildren: () => import('../modules/social-worker/social-worker.module').then(m => m.SocialWorkerModule) 
    },
    {
      path : 'pai-fisio', 
      component: BodyLayoutComponent,
      canActivate : [canViewFisioWorkerGuard],
      loadChildren: () => import('../modules/pai-fisio/pai-fisio.module').then(m => m.PaiFisioModule) 
    },
    {
      path : 'pai-psico',
      component: BodyLayoutComponent,
      canActivate : [canViewDocsPsicoGuard],
      loadChildren: () => import('../modules/pai-psico/pai-psico.module').then(m => m.PaiPsicoModule) 
    },
    {
      path : 'users', 
      component: BodyLayoutComponent,
      canActivate : [canAccessMembersGuard],
      loadChildren: () => import('../modules/users/users.module').then(m => m.UsersModule) 
    },
    {
      path : 'members', 
      component: BodyLayoutComponent,
      canActivate : [canAccessMembersGuard],
      loadChildren: () => import('../modules/members/members.module').then(m => m.MembersModule) 
    },
    {
      path : 'invoices', 
      component: BodyLayoutComponent,
      canActivate : [canAccessInvoicesGuard],
      loadChildren: () => import('../modules/invoices/invoices.module').then(m => m.InvoicesModule) 
    },
    {
      path : 'atenciones', 
      component: BodyLayoutComponent,
      canActivate : [canAccessAtencionesGuard],
      loadChildren: () => import('../modules/atenciones/atenciones.module').then(m => m.AtencionesModule) 
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
