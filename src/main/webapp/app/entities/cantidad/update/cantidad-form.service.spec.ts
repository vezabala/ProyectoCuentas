import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../cantidad.test-samples';

import { CantidadFormService } from './cantidad-form.service';

describe('Cantidad Form Service', () => {
  let service: CantidadFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CantidadFormService);
  });

  describe('Service methods', () => {
    describe('createCantidadFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCantidadFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombreCuenta: expect.any(Object),
            cantidadActual: expect.any(Object),
          })
        );
      });

      it('passing ICantidad should create a new form with FormGroup', () => {
        const formGroup = service.createCantidadFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombreCuenta: expect.any(Object),
            cantidadActual: expect.any(Object),
          })
        );
      });
    });

    describe('getCantidad', () => {
      it('should return NewCantidad for default Cantidad initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCantidadFormGroup(sampleWithNewData);

        const cantidad = service.getCantidad(formGroup) as any;

        expect(cantidad).toMatchObject(sampleWithNewData);
      });

      it('should return NewCantidad for empty Cantidad initial value', () => {
        const formGroup = service.createCantidadFormGroup();

        const cantidad = service.getCantidad(formGroup) as any;

        expect(cantidad).toMatchObject({});
      });

      it('should return ICantidad', () => {
        const formGroup = service.createCantidadFormGroup(sampleWithRequiredData);

        const cantidad = service.getCantidad(formGroup) as any;

        expect(cantidad).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICantidad should not enable id FormControl', () => {
        const formGroup = service.createCantidadFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCantidad should disable id FormControl', () => {
        const formGroup = service.createCantidadFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
