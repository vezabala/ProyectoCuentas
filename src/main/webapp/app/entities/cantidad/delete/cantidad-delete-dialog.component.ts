import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICantidad } from '../cantidad.model';
import { CantidadService } from '../service/cantidad.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './cantidad-delete-dialog.component.html',
})
export class CantidadDeleteDialogComponent {
  cantidad?: ICantidad;

  constructor(protected cantidadService: CantidadService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cantidadService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
