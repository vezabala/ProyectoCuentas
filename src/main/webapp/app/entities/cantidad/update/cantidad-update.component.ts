import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { CantidadFormService, CantidadFormGroup } from './cantidad-form.service';
import { ICantidad } from '../cantidad.model';
import { CantidadService } from '../service/cantidad.service';

@Component({
  selector: 'jhi-cantidad-update',
  templateUrl: './cantidad-update.component.html',
})
export class CantidadUpdateComponent implements OnInit {
  isSaving = false;
  cantidad: ICantidad | null = null;

  editForm: CantidadFormGroup = this.cantidadFormService.createCantidadFormGroup();

  constructor(
    protected cantidadService: CantidadService,
    protected cantidadFormService: CantidadFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cantidad }) => {
      this.cantidad = cantidad;
      if (cantidad) {
        this.updateForm(cantidad);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cantidad = this.cantidadFormService.getCantidad(this.editForm);
    if (cantidad.id !== null) {
      this.subscribeToSaveResponse(this.cantidadService.update(cantidad));
    } else {
      this.subscribeToSaveResponse(this.cantidadService.create(cantidad));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICantidad>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(cantidad: ICantidad): void {
    this.cantidad = cantidad;
    this.cantidadFormService.resetForm(this.editForm, cantidad);
  }
}
