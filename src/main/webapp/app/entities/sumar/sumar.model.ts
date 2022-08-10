import dayjs from 'dayjs/esm';
import { ICantidad } from 'app/entities/cantidad/cantidad.model';

export interface ISumar {
  id: number;
  cantidadSumar?: number | null;
  fecha?: dayjs.Dayjs | null;
  cantidad?: Pick<ICantidad, 'id'> | null;
}

export type NewSumar = Omit<ISumar, 'id'> & { id: null };
