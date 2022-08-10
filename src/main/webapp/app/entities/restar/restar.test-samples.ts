import dayjs from 'dayjs/esm';

import { IRestar, NewRestar } from './restar.model';

export const sampleWithRequiredData: IRestar = {
  id: 48834,
  cantidadRestar: 61313,
  fecha: dayjs('2022-08-10'),
};

export const sampleWithPartialData: IRestar = {
  id: 66372,
  cantidadRestar: 27673,
  fecha: dayjs('2022-08-09'),
};

export const sampleWithFullData: IRestar = {
  id: 50473,
  cantidadRestar: 89054,
  fecha: dayjs('2022-08-09'),
};

export const sampleWithNewData: NewRestar = {
  cantidadRestar: 36089,
  fecha: dayjs('2022-08-09'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
