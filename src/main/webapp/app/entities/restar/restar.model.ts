import dayjs from 'dayjs/esm';
import { ICantidad } from 'app/entities/cantidad/cantidad.model';

export interface IRestar {
  id: number;
  cantidadRestar?: number | null;
  fecha?: dayjs.Dayjs | null;
  cantidad?: Pick<ICantidad, 'id'> | null;
}

export type NewRestar = Omit<IRestar, 'id'> & { id: null };
