export interface ICantidad {
  id: number;
  nombreCuenta?: string | null;
  cantidadActual?: number | null;
}

export type NewCantidad = Omit<ICantidad, 'id'> & { id: null };
