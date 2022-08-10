import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'cantidad',
        data: { pageTitle: 'Cantidads' },
        loadChildren: () => import('./cantidad/cantidad.module').then(m => m.CantidadModule),
      },
      {
        path: 'sumar',
        data: { pageTitle: 'Sumars' },
        loadChildren: () => import('./sumar/sumar.module').then(m => m.SumarModule),
      },
      {
        path: 'restar',
        data: { pageTitle: 'Restars' },
        loadChildren: () => import('./restar/restar.module').then(m => m.RestarModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
