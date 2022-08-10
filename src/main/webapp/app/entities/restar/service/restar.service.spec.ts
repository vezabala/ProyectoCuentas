import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IRestar } from '../restar.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../restar.test-samples';

import { RestarService, RestRestar } from './restar.service';

const requireRestSample: RestRestar = {
  ...sampleWithRequiredData,
  fecha: sampleWithRequiredData.fecha?.format(DATE_FORMAT),
};

describe('Restar Service', () => {
  let service: RestarService;
  let httpMock: HttpTestingController;
  let expectedResult: IRestar | IRestar[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RestarService);
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

    it('should create a Restar', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const restar = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(restar).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Restar', () => {
      const restar = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(restar).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Restar', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Restar', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Restar', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRestarToCollectionIfMissing', () => {
      it('should add a Restar to an empty array', () => {
        const restar: IRestar = sampleWithRequiredData;
        expectedResult = service.addRestarToCollectionIfMissing([], restar);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(restar);
      });

      it('should not add a Restar to an array that contains it', () => {
        const restar: IRestar = sampleWithRequiredData;
        const restarCollection: IRestar[] = [
          {
            ...restar,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRestarToCollectionIfMissing(restarCollection, restar);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Restar to an array that doesn't contain it", () => {
        const restar: IRestar = sampleWithRequiredData;
        const restarCollection: IRestar[] = [sampleWithPartialData];
        expectedResult = service.addRestarToCollectionIfMissing(restarCollection, restar);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(restar);
      });

      it('should add only unique Restar to an array', () => {
        const restarArray: IRestar[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const restarCollection: IRestar[] = [sampleWithRequiredData];
        expectedResult = service.addRestarToCollectionIfMissing(restarCollection, ...restarArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const restar: IRestar = sampleWithRequiredData;
        const restar2: IRestar = sampleWithPartialData;
        expectedResult = service.addRestarToCollectionIfMissing([], restar, restar2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(restar);
        expect(expectedResult).toContain(restar2);
      });

      it('should accept null and undefined values', () => {
        const restar: IRestar = sampleWithRequiredData;
        expectedResult = service.addRestarToCollectionIfMissing([], null, restar, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(restar);
      });

      it('should return initial array if no Restar is added', () => {
        const restarCollection: IRestar[] = [sampleWithRequiredData];
        expectedResult = service.addRestarToCollectionIfMissing(restarCollection, undefined, null);
        expect(expectedResult).toEqual(restarCollection);
      });
    });

    describe('compareRestar', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRestar(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRestar(entity1, entity2);
        const compareResult2 = service.compareRestar(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRestar(entity1, entity2);
        const compareResult2 = service.compareRestar(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRestar(entity1, entity2);
        const compareResult2 = service.compareRestar(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
