import { Component, effect, input, output, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputText } from 'primeng/inputtext';
import { Select } from 'primeng/select';
import { Button } from 'primeng/button';
import { Dialog } from 'primeng/dialog';
import { Color, Country, Location, Person } from '../../model/Model';
import { greaterThan, notBlank } from '../validators';
import { ChooseExistingTable } from '../choose-existing-table/choose-existing-table';
import { LocationForm } from '../location-form/location-form';

@Component({
  selector: 'app-person-form',
  imports: [ReactiveFormsModule, InputText, Select, Button, Dialog, ChooseExistingTable, LocationForm],
  templateUrl: './person-form.html',
  styleUrl: '../form.scss',
})
export class PersonForm {
  initial = input<Person | null>(null);
  submitLabel = input('Save');
  saved = output<Person>();

  protected colors: Color[] = ['RED', 'BLUE', 'ORANGE', 'WHITE'];
  protected countries: Country[] = ['USA', 'SPAIN', 'SOUTH_KOREA', 'NORTH_KOREA'];

  protected existingLocationVisible = signal(false);
  protected newLocationVisible = signal(false);

  protected form = new FormGroup({
    name: new FormControl('', [Validators.required, notBlank]),
    eyeColor: new FormControl<Color | null>(null, Validators.required),
    hairColor: new FormControl<Color | null>(null),
    height: new FormControl<number | null>(null, [Validators.required, greaterThan(0)]),
    nationality: new FormControl<Country | null>(null),
    location: new FormControl<Location | null>(null, Validators.required),
  });

  constructor() {
    effect(() => {
      const p = this.initial();
      if (p) this.form.reset(p);
    });
  }

  protected setLocation(location: Location) {
    this.form.patchValue({ location });
    this.newLocationVisible.set(false);
  }

  protected locationLabel(l: Location) {
    const text = `${l.name ?? 'No name'} (${l.x}, ${l.y}, ${l.z})`;
    return l.id != null ? `#${l.id} ${text}` : `${text} — new`;
  }

  protected submit() {
    if (this.form.invalid) return;
    this.saved.emit({ ...this.initial(), ...this.form.getRawValue() } as Person);
  }
}
