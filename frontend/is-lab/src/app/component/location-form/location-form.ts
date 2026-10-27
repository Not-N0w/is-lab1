import { Component, effect, input, output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputText } from 'primeng/inputtext';
import { Button } from 'primeng/button';
import { Location } from '../../model/Model';

@Component({
  selector: 'app-location-form',
  imports: [ReactiveFormsModule, InputText, Button],
  templateUrl: './location-form.html',
  styleUrl: '../form.scss',
})
export class LocationForm {
  initial = input<Location | null>(null);
  submitLabel = input('Save');
  saved = output<Location>();

  protected form = new FormGroup({
    name: new FormControl<string | null>(null),
    x: new FormControl<number | null>(null, Validators.required),
    y: new FormControl<number | null>(null, Validators.required),
    z: new FormControl<number | null>(null, Validators.required),
  });

  constructor() {
    effect(() => {
      const l = this.initial();
      if (l) this.form.reset(l);
    });
  }

  protected submit() {
    if (this.form.invalid) return;
    const value = this.form.getRawValue();
    this.saved.emit({ ...this.initial(), ...value, name: value.name || null } as Location);
  }
}
