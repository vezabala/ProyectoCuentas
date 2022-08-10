import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RestarComponent } from '../list/restar.component';
import { RestarDetailComponent } from '../detail/restar-detail.component';
import { RestarUpdateComponent } from '../update/restar-update.component';
import { RestarRoutingResolveService } from './restar-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const restarRoute: Routes = [
  {
    path: '',
    component: RestarComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RestarDetailComponent,
    resolve: {
      restar: RestarRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RestarUpdateComponent,
    resolve: {
      restar: RestarRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RestarUpdateComponent,
    resolve: {
      restar: RestarRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(restarRoute)],
  exports: [RouterModule],
})
export class RestarRoutingModule {}
