import dayjs from 'dayjs/esm';

import { ISumar, NewSumar } from './sumar.model';

export const sampleWithRequiredData: ISumar = {
  id: 10878,
  cantidadSumar: 61094,
  fecha: dayjs('2022-08-09'),
};

export const sampleWithPartialData: ISumar = {
  id: 97534,
  cantidadSumar: 57323,
  fecha: dayjs('2022-08-09'),
};

export const sampleWithFullData: ISumar = {
  id: 29540,
  cantidadSumar: 56189,
  fecha: dayjs('2022-08-09'),
};

export const sampleWithNewData: NewSumar = {
  cantidadSumar: 53336,
  fecha: dayjs('2022-08-09'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
