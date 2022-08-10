import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICantidad, NewCantidad } from '../cantidad.model';

export type PartialUpdateCantidad = Partial<ICantidad> & Pick<ICantidad, 'id'>;

export type EntityResponseType = HttpResponse<ICantidad>;
export type EntityArrayResponseType = HttpResponse<ICantidad[]>;

@Injectable({ providedIn: 'root' })
export class CantidadService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cantidads');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cantidad: NewCantidad): Observable<EntityResponseType> {
    return this.http.post<ICantidad>(this.resourceUrl, cantidad, { observe: 'response' });
  }

  update(cantidad: ICantidad): Observable<EntityResponseType> {
    return this.http.put<ICantidad>(`${this.resourceUrl}/${this.getCantidadIdentifier(cantidad)}`, cantidad, { observe: 'response' });
  }

  partialUpdate(cantidad: PartialUpdateCantidad): Observable<EntityResponseType> {
    return this.http.patch<ICantidad>(`${this.resourceUrl}/${this.getCantidadIdentifier(cantidad)}`, cantidad, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICantidad>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICantidad[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCantidadIdentifier(cantidad: Pick<ICantidad, 'id'>): number {
    return cantidad.id;
  }

  compareCantidad(o1: Pick<ICantidad, 'id'> | null, o2: Pick<ICantidad, 'id'> | null): boolean {
    return o1 && o2 ? this.getCantidadIdentifier(o1) === this.getCantidadIdentifier(o2) : o1 === o2;
  }

  addCantidadToCollectionIfMissing<Type extends Pick<ICantidad, 'id'>>(
    cantidadCollection: Type[],
    ...cantidadsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cantidads: Type[] = cantidadsToCheck.filter(isPresent);
    if (cantidads.length > 0) {
      const cantidadCollectionIdentifiers = cantidadCollection.map(cantidadItem => this.getCantidadIdentifier(cantidadItem)!);
      const cantidadsToAdd = cantidads.filter(cantidadItem => {
        const cantidadIdentifier = this.getCantidadIdentifier(cantidadItem);
        if (cantidadCollectionIdentifiers.includes(cantidadIdentifier)) {
          return false;
        }
        cantidadCollectionIdentifiers.push(cantidadIdentifier);
        return true;
      });
      return [...cantidadsToAdd, ...cantidadCollection];
    }
    return cantidadCollection;
  }
}
