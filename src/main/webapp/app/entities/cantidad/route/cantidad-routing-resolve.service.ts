import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICantidad } from '../cantidad.model';
import { CantidadService } from '../service/cantidad.service';

@Injectable({ providedIn: 'root' })
export class CantidadRoutingResolveService implements Resolve<ICantidad | null> {
  constructor(protected service: CantidadService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICantidad | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cantidad: HttpResponse<ICantidad>) => {
          if (cantidad.body) {
            return of(cantidad.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
