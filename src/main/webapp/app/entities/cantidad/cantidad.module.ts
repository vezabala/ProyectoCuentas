import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CantidadComponent } from './list/cantidad.component';
import { CantidadDetailComponent } from './detail/cantidad-detail.component';
import { CantidadUpdateComponent } from './update/cantidad-update.component';
import { CantidadDeleteDialogComponent } from './delete/cantidad-delete-dialog.component';
import { CantidadRoutingModule } from './route/cantidad-routing.module';

@NgModule({
  imports: [SharedModule, CantidadRoutingModule],
  declarations: [CantidadComponent, CantidadDetailComponent, CantidadUpdateComponent, CantidadDeleteDialogComponent],
})
export class CantidadModule {}
