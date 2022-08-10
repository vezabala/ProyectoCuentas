import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRestar } from '../restar.model';

@Component({
  selector: 'jhi-restar-detail',
  templateUrl: './restar-detail.component.html',
})
export class RestarDetailComponent implements OnInit {
  restar: IRestar | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restar }) => {
      this.restar = restar;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
