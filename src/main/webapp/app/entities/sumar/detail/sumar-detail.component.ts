import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISumar } from '../sumar.model';

@Component({
  selector: 'jhi-sumar-detail',
  templateUrl: './sumar-detail.component.html',
})
export class SumarDetailComponent implements OnInit {
  sumar: ISumar | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sumar }) => {
      this.sumar = sumar;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
