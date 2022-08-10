import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SumarComponent } from '../list/sumar.component';
import { SumarDetailComponent } from '../detail/sumar-detail.component';
import { SumarUpdateComponent } from '../update/sumar-update.component';
import { SumarRoutingResolveService } from './sumar-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const sumarRoute: Routes = [
  {
    path: '',
    component: SumarComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SumarDetailComponent,
    resolve: {
      sumar: SumarRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SumarUpdateComponent,
    resolve: {
      sumar: SumarRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SumarUpdateComponent,
    resolve: {
      sumar: SumarRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sumarRoute)],
  exports: [RouterModule],
})
export class SumarRoutingModule {}
