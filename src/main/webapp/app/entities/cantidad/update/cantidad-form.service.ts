import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICantidad, NewCantidad } from '../cantidad.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICantidad for edit and NewCantidadFormGroupInput for create.
 */
type CantidadFormGroupInput = ICantidad | PartialWithRequiredKeyOf<NewCantidad>;

type CantidadFormDefaults = Pick<NewCantidad, 'id'>;

type CantidadFormGroupContent = {
  id: FormControl<ICantidad['id'] | NewCantidad['id']>;
  nombreCuenta: FormControl<ICantidad['nombreCuenta']>;
  cantidadActual: FormControl<ICantidad['cantidadActual']>;
};

export type CantidadFormGroup = FormGroup<CantidadFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CantidadFormService {
  createCantidadFormGroup(cantidad: CantidadFormGroupInput = { id: null }): CantidadFormGroup {
    const cantidadRawValue = {
      ...this.getFormDefaults(),
      ...cantidad,
    };
    return new FormGroup<CantidadFormGroupContent>({
      id: new FormControl(
        { value: cantidadRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombreCuenta: new FormControl(cantidadRawValue.nombreCuenta, {
        validators: [Validators.required],
      }),
      cantidadActual: new FormControl(cantidadRawValue.cantidadActual, {
        validators: [Validators.required],
      }),
    });
  }

  getCantidad(form: CantidadFormGroup): ICantidad | NewCantidad {
    return form.getRawValue() as ICantidad | NewCantidad;
  }

  resetForm(form: CantidadFormGroup, cantidad: CantidadFormGroupInput): void {
    const cantidadRawValue = { ...this.getFormDefaults(), ...cantidad };
    form.reset(
      {
        ...cantidadRawValue,
        id: { value: cantidadRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CantidadFormDefaults {
    return {
      id: null,
    };
  }
}
