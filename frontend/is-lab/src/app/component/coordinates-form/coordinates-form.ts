import { Component, effect, input, output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputText } from 'primeng/inputtext';
import { Button } from 'primeng/button';
import { Coordinates } from '../../model/Model';
import { greaterThan, integer } from '../validators';

@Component({
  selector: 'app-coordinates-form',
  imports: [ReactiveFormsModule, InputText, Button],
  templateUrl: './coordinates-form.html',
  styleUrl: '../form.scss',
})
export class CoordinatesForm {
  initial = input<Coordinates | null>(null);
  submitLabel = input('Save');
  saved = output<Coordinates>();

  protected form = new FormGroup({
    x: new FormControl<number | null>(null, [Validators.required, integer, greaterThan(-212)]),
    y: new FormControl<number | null>(null, [Validators.required, integer]),
  });

  constructor() {
    effect(() => {
      const c = this.initial();
      if (c) this.form.reset(c);
    });
  }

  protected submit() {
    if (this.form.invalid) return;
    this.saved.emit({ ...this.initial(), ...this.form.getRawValue() } as Coordinates);
  }
}
