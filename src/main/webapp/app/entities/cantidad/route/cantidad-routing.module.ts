import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CantidadComponent } from '../list/cantidad.component';
import { CantidadDetailComponent } from '../detail/cantidad-detail.component';
import { CantidadUpdateComponent } from '../update/cantidad-update.component';
import { CantidadRoutingResolveService } from './cantidad-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const cantidadRoute: Routes = [
  {
    path: '',
    component: CantidadComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CantidadDetailComponent,
    resolve: {
      cantidad: CantidadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CantidadUpdateComponent,
    resolve: {
      cantidad: CantidadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CantidadUpdateComponent,
    resolve: {
      cantidad: CantidadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cantidadRoute)],
  exports: [RouterModule],
})
export class CantidadRoutingModule {}
