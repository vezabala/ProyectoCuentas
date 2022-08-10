import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SumarComponent } from './list/sumar.component';
import { SumarDetailComponent } from './detail/sumar-detail.component';
import { SumarUpdateComponent } from './update/sumar-update.component';
import { SumarDeleteDialogComponent } from './delete/sumar-delete-dialog.component';
import { SumarRoutingModule } from './route/sumar-routing.module';

@NgModule({
  imports: [SharedModule, SumarRoutingModule],
  declarations: [SumarComponent, SumarDetailComponent, SumarUpdateComponent, SumarDeleteDialogComponent],
})
export class SumarModule {}
