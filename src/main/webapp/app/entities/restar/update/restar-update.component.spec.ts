import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RestarFormService } from './restar-form.service';
import { RestarService } from '../service/restar.service';
import { IRestar } from '../restar.model';
import { ICantidad } from 'app/entities/cantidad/cantidad.model';
import { CantidadService } from 'app/entities/cantidad/service/cantidad.service';

import { RestarUpdateComponent } from './restar-update.component';

describe('Restar Management Update Component', () => {
  let comp: RestarUpdateComponent;
  let fixture: ComponentFixture<RestarUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let restarFormService: RestarFormService;
  let restarService: RestarService;
  let cantidadService: CantidadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RestarUpdateComponent],
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
      .overrideTemplate(RestarUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RestarUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    restarFormService = TestBed.inject(RestarFormService);
    restarService = TestBed.inject(RestarService);
    cantidadService = TestBed.inject(CantidadService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cantidad query and add missing value', () => {
      const restar: IRestar = { id: 456 };
      const cantidad: ICantidad = { id: 91634 };
      restar.cantidad = cantidad;

      const cantidadCollection: ICantidad[] = [{ id: 22058 }];
      jest.spyOn(cantidadService, 'query').mockReturnValue(of(new HttpResponse({ body: cantidadCollection })));
      const additionalCantidads = [cantidad];
      const expectedCollection: ICantidad[] = [...additionalCantidads, ...cantidadCollection];
      jest.spyOn(cantidadService, 'addCantidadToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ restar });
      comp.ngOnInit();

      expect(cantidadService.query).toHaveBeenCalled();
      expect(cantidadService.addCantidadToCollectionIfMissing).toHaveBeenCalledWith(
        cantidadCollection,
        ...additionalCantidads.map(expect.objectContaining)
      );
      expect(comp.cantidadsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const restar: IRestar = { id: 456 };
      const cantidad: ICantidad = { id: 30978 };
      restar.cantidad = cantidad;

      activatedRoute.data = of({ restar });
      comp.ngOnInit();

      expect(comp.cantidadsSharedCollection).toContain(cantidad);
      expect(comp.restar).toEqual(restar);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRestar>>();
      const restar = { id: 123 };
      jest.spyOn(restarFormService, 'getRestar').mockReturnValue(restar);
      jest.spyOn(restarService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restar });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: restar }));
      saveSubject.complete();

      // THEN
      expect(restarFormService.getRestar).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(restarService.update).toHaveBeenCalledWith(expect.objectContaining(restar));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRestar>>();
      const restar = { id: 123 };
      jest.spyOn(restarFormService, 'getRestar').mockReturnValue({ id: null });
      jest.spyOn(restarService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restar: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: restar }));
      saveSubject.complete();

      // THEN
      expect(restarFormService.getRestar).toHaveBeenCalled();
      expect(restarService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRestar>>();
      const restar = { id: 123 };
      jest.spyOn(restarService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restar });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(restarService.update).toHaveBeenCalled();
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
