import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SumarFormService } from './sumar-form.service';
import { SumarService } from '../service/sumar.service';
import { ISumar } from '../sumar.model';
import { ICantidad } from 'app/entities/cantidad/cantidad.model';
import { CantidadService } from 'app/entities/cantidad/service/cantidad.service';

import { SumarUpdateComponent } from './sumar-update.component';

describe('Sumar Management Update Component', () => {
  let comp: SumarUpdateComponent;
  let fixture: ComponentFixture<SumarUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sumarFormService: SumarFormService;
  let sumarService: SumarService;
  let cantidadService: CantidadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SumarUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SumarUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SumarUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sumarFormService = TestBed.inject(SumarFormService);
    sumarService = TestBed.inject(SumarService);
    cantidadService = TestBed.inject(CantidadService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cantidad query and add missing value', () => {
      const sumar: ISumar = { id: 456 };
      const cantidad: ICantidad = { id: 96052 };
      sumar.cantidad = cantidad;

      const cantidadCollection: ICantidad[] = [{ id: 32271 }];
      jest.spyOn(cantidadService, 'query').mockReturnValue(of(new HttpResponse({ body: cantidadCollection })));
      const additionalCantidads = [cantidad];
      const expectedCollection: ICantidad[] = [...additionalCantidads, ...cantidadCollection];
      jest.spyOn(cantidadService, 'addCantidadToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sumar });
      comp.ngOnInit();

      expect(cantidadService.query).toHaveBeenCalled();
      expect(cantidadService.addCantidadToCollectionIfMissing).toHaveBeenCalledWith(
        cantidadCollection,
        ...additionalCantidads.map(expect.objectContaining)
      );
      expect(comp.cantidadsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sumar: ISumar = { id: 456 };
      const cantidad: ICantidad = { id: 35886 };
      sumar.cantidad = cantidad;

      activatedRoute.data = of({ sumar });
      comp.ngOnInit();

      expect(comp.cantidadsSharedCollection).toContain(cantidad);
      expect(comp.sumar).toEqual(sumar);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISumar>>();
      const sumar = { id: 123 };
      jest.spyOn(sumarFormService, 'getSumar').mockReturnValue(sumar);
      jest.spyOn(sumarService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sumar });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sumar }));
      saveSubject.complete();

      // THEN
      expect(sumarFormService.getSumar).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sumarService.update).toHaveBeenCalledWith(expect.objectContaining(sumar));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISumar>>();
      const sumar = { id: 123 };
      jest.spyOn(sumarFormService, 'getSumar').mockReturnValue({ id: null });
      jest.spyOn(sumarService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sumar: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sumar }));
      saveSubject.complete();

      // THEN
      expect(sumarFormService.getSumar).toHaveBeenCalled();
      expect(sumarService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISumar>>();
      const sumar = { id: 123 };
      jest.spyOn(sumarService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sumar });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sumarService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCantidad', () => {
      it('Should forward to cantidadService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(cantidadService, 'compareCantidad');
        comp.compareCantidad(entity, entity2);
        expect(cantidadService.compareCantidad).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
