import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRestar, NewRestar } from '../restar.model';

export type PartialUpdateRestar = Partial<IRestar> & Pick<IRestar, 'id'>;

type RestOf<T extends IRestar | NewRestar> = Omit<T, 'fecha'> & {
  fecha?: string | null;
};

export type RestRestar = RestOf<IRestar>;

export type NewRestRestar = RestOf<NewRestar>;

export type PartialUpdateRestRestar = RestOf<PartialUpdateRestar>;

export type EntityResponseType = HttpResponse<IRestar>;
export type EntityArrayResponseType = HttpResponse<IRestar[]>;

@Injectable({ providedIn: 'root' })
export class RestarService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/restars');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(restar: NewRestar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(restar);
    return this.http
      .post<RestRestar>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(restar: IRestar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(restar);
    return this.http
      .put<RestRestar>(`${this.resourceUrl}/${this.getRestarIdentifier(restar)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(restar: PartialUpdateRestar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(restar);
    return this.http
      .patch<RestRestar>(`${this.resourceUrl}/${this.getRestarIdentifier(restar)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRestar>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRestar[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRestarIdentifier(restar: Pick<IRestar, 'id'>): number {
    return restar.id;
  }

  compareRestar(o1: Pick<IRestar, 'id'> | null, o2: Pick<IRestar, 'id'> | null): boolean {
    return o1 && o2 ? this.getRestarIdentifier(o1) === this.getRestarIdentifier(o2) : o1 === o2;
  }

  addRestarToCollectionIfMissing<Type extends Pick<IRestar, 'id'>>(
    restarCollection: Type[],
    ...restarsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const restars: Type[] = restarsToCheck.filter(isPresent);
    if (restars.length > 0) {
      const restarCollectionIdentifiers = restarCollection.map(restarItem => this.getRestarIdentifier(restarItem)!);
      const restarsToAdd = restars.filter(restarItem => {
        const restarIdentifier = this.getRestarIdentifier(restarItem);
        if (restarCollectionIdentifiers.includes(restarIdentifier)) {
          return false;
        }
        restarCollectionIdentifiers.push(restarIdentifier);
        return true;
      });
      return [...restarsToAdd, ...restarCollection];
    }
    return restarCollection;
  }

  protected convertDateFromClient<T extends IRestar | NewRestar | PartialUpdateRestar>(restar: T): RestOf<T> {
    return {
      ...restar,
      fecha: restar.fecha?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restRestar: RestRestar): IRestar {
    return {
      ...restRestar,
      fecha: restRestar.fecha ? dayjs(restRestar.fecha) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRestar>): HttpResponse<IRestar> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRestar[]>): HttpResponse<IRestar[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
