import { Component, effect, input, output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputText } from 'primeng/inputtext';
import { Button } from 'primeng/button';
import { Discipline } from '../../model/Model';
import { integer, notBlank } from '../validators';

@Component({
  selector: 'app-discipline-form',
  imports: [ReactiveFormsModule, InputText, Button],
  templateUrl: './discipline-form.html',
  styleUrl: '../form.scss',
})
export class DisciplineForm {
  initial = input<Discipline | null>(null);
  submitLabel = input('Save');
  saved = output<Discipline>();

  protected form = new FormGroup({
    name: new FormControl('', [Validators.required, notBlank]),
    practiceHours: new FormControl<number | null>(null, integer),
  });

  constructor() {
    effect(() => {
      const d = this.initial();
      if (d) this.form.reset(d);
    });
  }

  protected submit() {
    if (this.form.invalid) return;
    this.saved.emit({ ...this.initial(), ...this.form.getRawValue() } as Discipline);
  }
}
