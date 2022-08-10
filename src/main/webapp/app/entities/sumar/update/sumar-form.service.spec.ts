import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../sumar.test-samples';

import { SumarFormService } from './sumar-form.service';

describe('Sumar Form Service', () => {
  let service: SumarFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SumarFormService);
  });

  describe('Service methods', () => {
    describe('createSumarFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSumarFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cantidadSumar: expect.any(Object),
            fecha: expect.any(Object),
            cantidad: expect.any(Object),
          })
        );
      });

      it('passing ISumar should create a new form with FormGroup', () => {
        const formGroup = service.createSumarFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cantidadSumar: expect.any(Object),
            fecha: expect.any(Object),
            cantidad: expect.any(Object),
          })
        );
      });
    });

    describe('getSumar', () => {
      it('should return NewSumar for default Sumar initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSumarFormGroup(sampleWithNewData);

        const sumar = service.getSumar(formGroup) as any;

        expect(sumar).toMatchObject(sampleWithNewData);
      });

      it('should return NewSumar for empty Sumar initial value', () => {
        const formGroup = service.createSumarFormGroup();

        const sumar = service.getSumar(formGroup) as any;

        expect(sumar).toMatchObject({});
      });

      it('should return ISumar', () => {
        const formGroup = service.createSumarFormGroup(sampleWithRequiredData);

        const sumar = service.getSumar(formGroup) as any;

        expect(sumar).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISumar should not enable id FormControl', () => {
        const formGroup = service.createSumarFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSumar should disable id FormControl', () => {
        const formGroup = service.createSumarFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
