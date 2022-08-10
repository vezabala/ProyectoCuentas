import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRestar, NewRestar } from '../restar.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRestar for edit and NewRestarFormGroupInput for create.
 */
type RestarFormGroupInput = IRestar | PartialWithRequiredKeyOf<NewRestar>;

type RestarFormDefaults = Pick<NewRestar, 'id'>;

type RestarFormGroupContent = {
  id: FormControl<IRestar['id'] | NewRestar['id']>;
  cantidadRestar: FormControl<IRestar['cantidadRestar']>;
  fecha: FormControl<IRestar['fecha']>;
  cantidad: FormControl<IRestar['cantidad']>;
};

export type RestarFormGroup = FormGroup<RestarFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RestarFormService {
  createRestarFormGroup(restar: RestarFormGroupInput = { id: null }): RestarFormGroup {
    const restarRawValue = {
      ...this.getFormDefaults(),
      ...restar,
    };
    return new FormGroup<RestarFormGroupContent>({
      id: new FormControl(
        { value: restarRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      cantidadRestar: new FormControl(restarRawValue.cantidadRestar, {
        validators: [Validators.required],
      }),
      fecha: new FormControl(restarRawValue.fecha, {
        validators: [Validators.required],
      }),
      cantidad: new FormControl(restarRawValue.cantidad, {
        validators: [Validators.required],
      }),
    });
  }

  getRestar(form: RestarFormGroup): IRestar | NewRestar {
    return form.getRawValue() as IRestar | NewRestar;
  }

  resetForm(form: RestarFormGroup, restar: RestarFormGroupInput): void {
    const restarRawValue = { ...this.getFormDefaults(), ...restar };
    form.reset(
      {
        ...restarRawValue,
        id: { value: restarRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RestarFormDefaults {
    return {
      id: null,
    };
  }
}
