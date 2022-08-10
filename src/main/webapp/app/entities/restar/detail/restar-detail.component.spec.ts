import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RestarDetailComponent } from './restar-detail.component';

describe('Restar Management Detail Component', () => {
  let comp: RestarDetailComponent;
  let fixture: ComponentFixture<RestarDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RestarDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ restar: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RestarDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RestarDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load restar on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.restar).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
