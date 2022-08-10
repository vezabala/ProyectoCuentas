import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISumar, NewSumar } from '../sumar.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISumar for edit and NewSumarFormGroupInput for create.
 */
type SumarFormGroupInput = ISumar | PartialWithRequiredKeyOf<NewSumar>;

type SumarFormDefaults = Pick<NewSumar, 'id'>;

type SumarFormGroupContent = {
  id: FormControl<ISumar['id'] | NewSumar['id']>;
  cantidadSumar: FormControl<ISumar['cantidadSumar']>;
  fecha: FormControl<ISumar['fecha']>;
  cantidad: FormControl<ISumar['cantidad']>;
};

export type SumarFormGroup = FormGroup<SumarFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SumarFormService {
  createSumarFormGroup(sumar: SumarFormGroupInput = { id: null }): SumarFormGroup {
    const sumarRawValue = {
      ...this.getFormDefaults(),
      ...sumar,
    };
    return new FormGroup<SumarFormGroupContent>({
      id: new FormControl(
        { value: sumarRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      cantidadSumar: new FormControl(sumarRawValue.cantidadSumar, {
        validators: [Validators.required],
      }),
      fecha: new FormControl(sumarRawValue.fecha, {
        validators: [Validators.required],
      }),
      cantidad: new FormControl(sumarRawValue.cantidad, {
        validators: [Validators.required],
      }),
    });
  }

  getSumar(form: SumarFormGroup): ISumar | NewSumar {
    return form.getRawValue() as ISumar | NewSumar;
  }

  resetForm(form: SumarFormGroup, sumar: SumarFormGroupInput): void {
    const sumarRawValue = { ...this.getFormDefaults(), ...sumar };
    form.reset(
      {
        ...sumarRawValue,
        id: { value: sumarRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SumarFormDefaults {
    return {
      id: null,
    };
  }
}
