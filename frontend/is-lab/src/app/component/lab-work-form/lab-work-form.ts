import { Component, effect, input, output, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputText } from 'primeng/inputtext';
import { Select } from 'primeng/select';
import { Textarea } from 'primeng/textarea';
import { Button } from 'primeng/button';
import { Dialog } from 'primeng/dialog';
import { Coordinates, Difficulty, Discipline, LabWork, Person } from '../../model/Model';
import { greaterThan, integer, notBlank } from '../validators';
import { ChooseExistingTable } from '../choose-existing-table/choose-existing-table';
import { CoordinatesForm } from '../coordinates-form/coordinates-form';
import { DisciplineForm } from '../discipline-form/discipline-form';
import { PersonForm } from '../person-form/person-form';

@Component({
  selector: 'app-lab-work-form',
  imports: [
    ReactiveFormsModule,
    InputText,
    Select,
    Textarea,
    Button,
    Dialog,
    ChooseExistingTable,
    CoordinatesForm,
    DisciplineForm,
    PersonForm,
  ],
  templateUrl: './lab-work-form.html',
  styleUrl: '../form.scss',
})
export class LabWorkForm {
  initial = input<LabWork | null>(null);
  submitLabel = input('Save');
  saved = output<LabWork>();

  protected difficulties: Difficulty[] = ['VERY_EASY', 'HARD', 'TERRIBLE'];

  protected existingCoordinatesVisible = signal(false);
  protected existingDisciplineVisible = signal(false);
  protected existingAuthorVisible = signal(false);
  protected newCoordinatesVisible = signal(false);
  protected newDisciplineVisible = signal(false);
  protected newAuthorVisible = signal(false);

  protected form = new FormGroup({
    name: new FormControl('', [Validators.required, notBlank]),
    difficulty: new FormControl<Difficulty | null>(null),
    minimalPoint: new FormControl<number | null>(null, [Validators.required, greaterThan(0)]),
    tunedInWorks: new FormControl<number | null>(null, [Validators.required, integer]),
    description: new FormControl<string | null>(null),
    coordinates: new FormControl<Coordinates | null>(null, Validators.required),
    discipline: new FormControl<Discipline | null>(null, Validators.required),
    author: new FormControl<Person | null>(null),
  });

  constructor() {
    effect(() => {
      const lw = this.initial();
      if (lw) this.form.reset(lw);
    });
  }

  protected setCoordinates(coordinates: Coordinates) {
    this.form.patchValue({ coordinates });
    this.newCoordinatesVisible.set(false);
  }

  protected setDiscipline(discipline: Discipline) {
    this.form.patchValue({ discipline });
    this.newDisciplineVisible.set(false);
  }

  protected setAuthor(author: Person) {
    this.form.patchValue({ author });
    this.newAuthorVisible.set(false);
  }

  protected label(id: number | undefined, text: string) {
    return id != null ? `#${id} ${text}` : `${text} — new`;
  }

  protected submit() {
    if (this.form.invalid) return;
    const value = this.form.getRawValue();
    this.saved.emit({ ...this.initial(), ...value, description: value.description || null } as LabWork);
  }
}
