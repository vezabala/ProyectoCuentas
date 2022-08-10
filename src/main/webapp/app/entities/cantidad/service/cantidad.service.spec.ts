import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICantidad } from '../cantidad.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../cantidad.test-samples';

import { CantidadService } from './cantidad.service';

const requireRestSample: ICantidad = {
  ...sampleWithRequiredData,
};

describe('Cantidad Service', () => {
  let service: CantidadService;
  let httpMock: HttpTestingController;
  let expectedResult: ICantidad | ICantidad[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CantidadService);
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

    it('should create a Cantidad', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const cantidad = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cantidad).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cantidad', () => {
      const cantidad = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cantidad).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cantidad', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cantidad', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Cantidad', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCantidadToCollectionIfMissing', () => {
      it('should add a Cantidad to an empty array', () => {
        const cantidad: ICantidad = sampleWithRequiredData;
        expectedResult = service.addCantidadToCollectionIfMissing([], cantidad);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cantidad);
      });

      it('should not add a Cantidad to an array that contains it', () => {
        const cantidad: ICantidad = sampleWithRequiredData;
        const cantidadCollection: ICantidad[] = [
          {
            ...cantidad,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCantidadToCollectionIfMissing(cantidadCollection, cantidad);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cantidad to an array that doesn't contain it", () => {
        const cantidad: ICantidad = sampleWithRequiredData;
        const cantidadCollection: ICantidad[] = [sampleWithPartialData];
        expectedResult = service.addCantidadToCollectionIfMissing(cantidadCollection, cantidad);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cantidad);
      });

      it('should add only unique Cantidad to an array', () => {
        const cantidadArray: ICantidad[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cantidadCollection: ICantidad[] = [sampleWithRequiredData];
        expectedResult = service.addCantidadToCollectionIfMissing(cantidadCollection, ...cantidadArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cantidad: ICantidad = sampleWithRequiredData;
        const cantidad2: ICantidad = sampleWithPartialData;
        expectedResult = service.addCantidadToCollectionIfMissing([], cantidad, cantidad2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cantidad);
        expect(expectedResult).toContain(cantidad2);
      });

      it('should accept null and undefined values', () => {
        const cantidad: ICantidad = sampleWithRequiredData;
        expectedResult = service.addCantidadToCollectionIfMissing([], null, cantidad, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cantidad);
      });

      it('should return initial array if no Cantidad is added', () => {
        const cantidadCollection: ICantidad[] = [sampleWithRequiredData];
        expectedResult = service.addCantidadToCollectionIfMissing(cantidadCollection, undefined, null);
        expect(expectedResult).toEqual(cantidadCollection);
      });
    });

    describe('compareCantidad', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCantidad(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCantidad(entity1, entity2);
        const compareResult2 = service.compareCantidad(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCantidad(entity1, entity2);
        const compareResult2 = service.compareCantidad(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCantidad(entity1, entity2);
        const compareResult2 = service.compareCantidad(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
