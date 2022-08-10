import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISumar } from '../sumar.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../sumar.test-samples';

import { SumarService, RestSumar } from './sumar.service';

const requireRestSample: RestSumar = {
  ...sampleWithRequiredData,
  fecha: sampleWithRequiredData.fecha?.format(DATE_FORMAT),
};

describe('Sumar Service', () => {
  let service: SumarService;
  let httpMock: HttpTestingController;
  let expectedResult: ISumar | ISumar[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SumarService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Sumar', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const sumar = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sumar).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Sumar', () => {
      const sumar = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sumar).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Sumar', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Sumar', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Sumar', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSumarToCollectionIfMissing', () => {
      it('should add a Sumar to an empty array', () => {
        const sumar: ISumar = sampleWithRequiredData;
        expectedResult = service.addSumarToCollectionIfMissing([], sumar);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sumar);
      });

      it('should not add a Sumar to an array that contains it', () => {
        const sumar: ISumar = sampleWithRequiredData;
        const sumarCollection: ISumar[] = [
          {
            ...sumar,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSumarToCollectionIfMissing(sumarCollection, sumar);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Sumar to an array that doesn't contain it", () => {
        const sumar: ISumar = sampleWithRequiredData;
        const sumarCollection: ISumar[] = [sampleWithPartialData];
        expectedResult = service.addSumarToCollectionIfMissing(sumarCollection, sumar);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sumar);
      });

      it('should add only unique Sumar to an array', () => {
        const sumarArray: ISumar[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const sumarCollection: ISumar[] = [sampleWithRequiredData];
        expectedResult = service.addSumarToCollectionIfMissing(sumarCollection, ...sumarArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sumar: ISumar = sampleWithRequiredData;
        const sumar2: ISumar = sampleWithPartialData;
        expectedResult = service.addSumarToCollectionIfMissing([], sumar, sumar2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sumar);
        expect(expectedResult).toContain(sumar2);
      });

      it('should accept null and undefined values', () => {
        const sumar: ISumar = sampleWithRequiredData;
        expectedResult = service.addSumarToCollectionIfMissing([], null, sumar, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sumar);
      });

      it('should return initial array if no Sumar is added', () => {
        const sumarCollection: ISumar[] = [sampleWithRequiredData];
        expectedResult = service.addSumarToCollectionIfMissing(sumarCollection, undefined, null);
        expect(expectedResult).toEqual(sumarCollection);
      });
    });

    describe('compareSumar', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSumar(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSumar(entity1, entity2);
        const compareResult2 = service.compareSumar(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSumar(entity1, entity2);
        const compareResult2 = service.compareSumar(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSumar(entity1, entity2);
        const compareResult2 = service.compareSumar(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
