import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { RestarFormService, RestarFormGroup } from './restar-form.service';
import { IRestar } from '../restar.model';
import { RestarService } from '../service/restar.service';
import { ICantidad } from 'app/entities/cantidad/cantidad.model';
import { CantidadService } from 'app/entities/cantidad/service/cantidad.service';

@Component({
  selector: 'jhi-restar-update',
  templateUrl: './restar-update.component.html',
})
export class RestarUpdateComponent implements OnInit {
  isSaving = false;
  restar: IRestar | null = null;

  cantidadsSharedCollection: ICantidad[] = [];

  editForm: RestarFormGroup = this.restarFormService.createRestarFormGroup();

  constructor(
    protected restarService: RestarService,
    protected restarFormService: RestarFormService,
    protected cantidadService: CantidadService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCantidad = (o1: ICantidad | null, o2: ICantidad | null): boolean => this.cantidadService.compareCantidad(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restar }) => {
      this.restar = restar;
      if (restar) {
        this.updateForm(restar);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const restar = this.restarFormService.getRestar(this.editForm);
    if (restar.id !== null) {
      this.subscribeToSaveResponse(this.restarService.update(restar));
    } else {
      this.subscribeToSaveResponse(this.restarService.create(restar));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRestar>>): void {
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

  protected updateForm(restar: IRestar): void {
    this.restar = restar;
    this.restarFormService.resetForm(this.editForm, restar);

    this.cantidadsSharedCollection = this.cantidadService.addCantidadToCollectionIfMissing<ICantidad>(
      this.cantidadsSharedCollection,
      restar.cantidad
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cantidadService
      .query()
      .pipe(map((res: HttpResponse<ICantidad[]>) => res.body ?? []))
      .pipe(
        map((cantidads: ICantidad[]) => this.cantidadService.addCantidadToCollectionIfMissing<ICantidad>(cantidads, this.restar?.cantidad))
      )
      .subscribe((cantidads: ICantidad[]) => (this.cantidadsSharedCollection = cantidads));
  }
}
