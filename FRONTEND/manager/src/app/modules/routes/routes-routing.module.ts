import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';


import { RelativeRouteComponent } from './relative-route/relative-route.component'; 
import { canViewRouteGuard } from 'src/app/core/guards/can-view-route.guard';

export const routes: Routes = [
    {
      path: '',
      children: [
        {
          path: 'relative-route',
          component: RelativeRouteComponent, // <---
          canActivate : [canViewRouteGuard]
        },
        {
            path: '**',
            redirectTo: 'login', // <---
        },
      ],
    },
  ];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
    //providers: [CanUpdateAcademicTitle]
})
export class RoutesRoutingModule { }