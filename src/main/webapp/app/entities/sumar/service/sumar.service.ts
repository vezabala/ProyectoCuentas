import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISumar, NewSumar } from '../sumar.model';

export type PartialUpdateSumar = Partial<ISumar> & Pick<ISumar, 'id'>;

type RestOf<T extends ISumar | NewSumar> = Omit<T, 'fecha'> & {
  fecha?: string | null;
};

export type RestSumar = RestOf<ISumar>;

export type NewRestSumar = RestOf<NewSumar>;

export type PartialUpdateRestSumar = RestOf<PartialUpdateSumar>;

export type EntityResponseType = HttpResponse<ISumar>;
export type EntityArrayResponseType = HttpResponse<ISumar[]>;

@Injectable({ providedIn: 'root' })
export class SumarService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sumars');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sumar: NewSumar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sumar);
    return this.http.post<RestSumar>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(sumar: ISumar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sumar);
    return this.http
      .put<RestSumar>(`${this.resourceUrl}/${this.getSumarIdentifier(sumar)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(sumar: PartialUpdateSumar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sumar);
    return this.http
      .patch<RestSumar>(`${this.resourceUrl}/${this.getSumarIdentifier(sumar)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSumar>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSumar[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSumarIdentifier(sumar: Pick<ISumar, 'id'>): number {
    return sumar.id;
  }

  compareSumar(o1: Pick<ISumar, 'id'> | null, o2: Pick<ISumar, 'id'> | null): boolean {
    return o1 && o2 ? this.getSumarIdentifier(o1) === this.getSumarIdentifier(o2) : o1 === o2;
  }

  addSumarToCollectionIfMissing<Type extends Pick<ISumar, 'id'>>(
    sumarCollection: Type[],
    ...sumarsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sumars: Type[] = sumarsToCheck.filter(isPresent);
    if (sumars.length > 0) {
      const sumarCollectionIdentifiers = sumarCollection.map(sumarItem => this.getSumarIdentifier(sumarItem)!);
      const sumarsToAdd = sumars.filter(sumarItem => {
        const sumarIdentifier = this.getSumarIdentifier(sumarItem);
        if (sumarCollectionIdentifiers.includes(sumarIdentifier)) {
          return false;
        }
        sumarCollectionIdentifiers.push(sumarIdentifier);
        return true;
      });
      return [...sumarsToAdd, ...sumarCollection];
    }
    return sumarCollection;
  }

  protected convertDateFromClient<T extends ISumar | NewSumar | PartialUpdateSumar>(sumar: T): RestOf<T> {
    return {
      ...sumar,
      fecha: sumar.fecha?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restSumar: RestSumar): ISumar {
    return {
      ...restSumar,
      fecha: restSumar.fecha ? dayjs(restSumar.fecha) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSumar>): HttpResponse<ISumar> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSumar[]>): HttpResponse<ISumar[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
