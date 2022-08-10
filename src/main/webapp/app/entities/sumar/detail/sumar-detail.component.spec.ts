import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SumarDetailComponent } from './sumar-detail.component';

describe('Sumar Management Detail Component', () => {
  let comp: SumarDetailComponent;
  let fixture: ComponentFixture<SumarDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SumarDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sumar: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SumarDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SumarDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sumar on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sumar).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
