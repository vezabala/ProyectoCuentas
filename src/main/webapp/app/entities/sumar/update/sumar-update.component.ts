import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SumarFormService, SumarFormGroup } from './sumar-form.service';
import { ISumar } from '../sumar.model';
import { SumarService } from '../service/sumar.service';
import { ICantidad } from 'app/entities/cantidad/cantidad.model';
import { CantidadService } from 'app/entities/cantidad/service/cantidad.service';

@Component({
  selector: 'jhi-sumar-update',
  templateUrl: './sumar-update.component.html',
})
export class SumarUpdateComponent implements OnInit {
  isSaving = false;
  sumar: ISumar | null = null;

  cantidadsSharedCollection: ICantidad[] = [];

  editForm: SumarFormGroup = this.sumarFormService.createSumarFormGroup();

  constructor(
    protected sumarService: SumarService,
    protected sumarFormService: SumarFormService,
    protected cantidadService: CantidadService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCantidad = (o1: ICantidad | null, o2: ICantidad | null): boolean => this.cantidadService.compareCantidad(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sumar }) => {
      this.sumar = sumar;
      if (sumar) {
        this.updateForm(sumar);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sumar = this.sumarFormService.getSumar(this.editForm);
    if (sumar.id !== null) {
      this.subscribeToSaveResponse(this.sumarService.update(sumar));
    } else {
      this.subscribeToSaveResponse(this.sumarService.create(sumar));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISumar>>): void {
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

  protected updateForm(sumar: ISumar): void {
    this.sumar = sumar;
    this.sumarFormService.resetForm(this.editForm, sumar);

    this.cantidadsSharedCollection = this.cantidadService.addCantidadToCollectionIfMissing<ICantidad>(
      this.cantidadsSharedCollection,
      sumar.cantidad
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cantidadService
      .query()
      .pipe(map((res: HttpResponse<ICantidad[]>) => res.body ?? []))
      .pipe(
        map((cantidads: ICantidad[]) => this.cantidadService.addCantidadToCollectionIfMissing<ICantidad>(cantidads, this.sumar?.cantidad))
      )
      .subscribe((cantidads: ICantidad[]) => (this.cantidadsSharedCollection = cantidads));
  }
}
