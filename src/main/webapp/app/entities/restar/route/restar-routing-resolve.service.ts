import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRestar } from '../restar.model';
import { RestarService } from '../service/restar.service';

@Injectable({ providedIn: 'root' })
export class RestarRoutingResolveService implements Resolve<IRestar | null> {
  constructor(protected service: RestarService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRestar | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((restar: HttpResponse<IRestar>) => {
          if (restar.body) {
            return of(restar.body);
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
