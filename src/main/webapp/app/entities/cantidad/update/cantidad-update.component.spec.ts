import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CantidadFormService } from './cantidad-form.service';
import { CantidadService } from '../service/cantidad.service';
import { ICantidad } from '../cantidad.model';

import { CantidadUpdateComponent } from './cantidad-update.component';

describe('Cantidad Management Update Component', () => {
  let comp: CantidadUpdateComponent;
  let fixture: ComponentFixture<CantidadUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cantidadFormService: CantidadFormService;
  let cantidadService: CantidadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CantidadUpdateComponent],
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
      .overrideTemplate(CantidadUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CantidadUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cantidadFormService = TestBed.inject(CantidadFormService);
    cantidadService = TestBed.inject(CantidadService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cantidad: ICantidad = { id: 456 };

      activatedRoute.data = of({ cantidad });
      comp.ngOnInit();

      expect(comp.cantidad).toEqual(cantidad);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICantidad>>();
      const cantidad = { id: 123 };
      jest.spyOn(cantidadFormService, 'getCantidad').mockReturnValue(cantidad);
      jest.spyOn(cantidadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cantidad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cantidad }));
      saveSubject.complete();

      // THEN
      expect(cantidadFormService.getCantidad).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cantidadService.update).toHaveBeenCalledWith(expect.objectContaining(cantidad));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICantidad>>();
      const cantidad = { id: 123 };
      jest.spyOn(cantidadFormService, 'getCantidad').mockReturnValue({ id: null });
      jest.spyOn(cantidadService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cantidad: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cantidad }));
      saveSubject.complete();

      // THEN
      expect(cantidadFormService.getCantidad).toHaveBeenCalled();
      expect(cantidadService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICantidad>>();
      const cantidad = { id: 123 };
      jest.spyOn(cantidadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cantidad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cantidadService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
