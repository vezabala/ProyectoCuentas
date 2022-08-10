import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CantidadDetailComponent } from './cantidad-detail.component';

describe('Cantidad Management Detail Component', () => {
  let comp: CantidadDetailComponent;
  let fixture: ComponentFixture<CantidadDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CantidadDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cantidad: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CantidadDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CantidadDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cantidad on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cantidad).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
