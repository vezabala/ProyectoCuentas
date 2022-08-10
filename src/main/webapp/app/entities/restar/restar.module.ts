import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RestarComponent } from './list/restar.component';
import { RestarDetailComponent } from './detail/restar-detail.component';
import { RestarUpdateComponent } from './update/restar-update.component';
import { RestarDeleteDialogComponent } from './delete/restar-delete-dialog.component';
import { RestarRoutingModule } from './route/restar-routing.module';

@NgModule({
  imports: [SharedModule, RestarRoutingModule],
  declarations: [RestarComponent, RestarDetailComponent, RestarUpdateComponent, RestarDeleteDialogComponent],
})
export class RestarModule {}
