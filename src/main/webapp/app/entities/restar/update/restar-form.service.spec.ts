import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../restar.test-samples';

import { RestarFormService } from './restar-form.service';

describe('Restar Form Service', () => {
  let service: RestarFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RestarFormService);
  });

  describe('Service methods', () => {
    describe('createRestarFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRestarFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cantidadRestar: expect.any(Object),
            fecha: expect.any(Object),
            cantidad: expect.any(Object),
          })
        );
      });

      it('passing IRestar should create a new form with FormGroup', () => {
        const formGroup = service.createRestarFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cantidadRestar: expect.any(Object),
            fecha: expect.any(Object),
            cantidad: expect.any(Object),
          })
        );
      });
    });

    describe('getRestar', () => {
      it('should return NewRestar for default Restar initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createRestarFormGroup(sampleWithNewData);

        const restar = service.getRestar(formGroup) as any;

        expect(restar).toMatchObject(sampleWithNewData);
      });

      it('should return NewRestar for empty Restar initial value', () => {
        const formGroup = service.createRestarFormGroup();

        const restar = service.getRestar(formGroup) as any;

        expect(restar).toMatchObject({});
      });

      it('should return IRestar', () => {
        const formGroup = service.createRestarFormGroup(sampleWithRequiredData);

        const restar = service.getRestar(formGroup) as any;

        expect(restar).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRestar should not enable id FormControl', () => {
        const formGroup = service.createRestarFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRestar should disable id FormControl', () => {
        const formGroup = service.createRestarFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
