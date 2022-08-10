import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICantidad } from '../cantidad.model';

@Component({
  selector: 'jhi-cantidad-detail',
  templateUrl: './cantidad-detail.component.html',
})
export class CantidadDetailComponent implements OnInit {
  cantidad: ICantidad | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cantidad }) => {
      this.cantidad = cantidad;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
