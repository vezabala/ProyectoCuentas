import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISumar } from '../sumar.model';
import { SumarService } from '../service/sumar.service';

@Injectable({ providedIn: 'root' })
export class SumarRoutingResolveService implements Resolve<ISumar | null> {
  constructor(protected service: SumarService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISumar | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sumar: HttpResponse<ISumar>) => {
          if (sumar.body) {
            return of(sumar.body);
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
