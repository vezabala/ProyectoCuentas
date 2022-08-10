import { ICantidad, NewCantidad } from './cantidad.model';

export const sampleWithRequiredData: ICantidad = {
  id: 99778,
  nombreCuenta: 'enhance gestión',
  cantidadActual: 29127,
};

export const sampleWithPartialData: ICantidad = {
  id: 54591,
  nombreCuenta: 'Región Peso',
  cantidadActual: 7243,
};

export const sampleWithFullData: ICantidad = {
  id: 76894,
  nombreCuenta: 'vortals Organizado',
  cantidadActual: 42866,
};

export const sampleWithNewData: NewCantidad = {
  nombreCuenta: 'quantifying Regional La',
  cantidadActual: 96675,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
