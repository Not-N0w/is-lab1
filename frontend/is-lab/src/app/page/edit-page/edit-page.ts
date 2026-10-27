import { Component, inject, input, numberAttribute, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Button } from 'primeng/button';
import { SelectButton } from 'primeng/selectbutton';
import {
  Coordinates,
  Discipline,
  LabWork,
  Location,
  Person,
  Strategy,
  toLabWorkRequest,
  toPersonRequest,
} from '../../model/Model';
import { LabWorkService } from '../../service/LabWorkService';
import { CoordinatesService } from '../../service/CoordinatesService';
import { DisciplineService } from '../../service/DisciplineService';
import { PersonService } from '../../service/PersonService';
import { LocationService } from '../../service/LocationService';
import { LabWorkForm } from '../../component/lab-work-form/lab-work-form';
import { CoordinatesForm } from '../../component/coordinates-form/coordinates-form';
import { DisciplineForm } from '../../component/discipline-form/discipline-form';
import { PersonForm } from '../../component/person-form/person-form';
import { LocationForm } from '../../component/location-form/location-form';
import { Location as BrowserLocation } from '@angular/common';

@Component({
  selector: 'app-edit-page',
  imports: [
    FormsModule,
    Button,
    SelectButton,
    LabWorkForm,
    CoordinatesForm,
    DisciplineForm,
    PersonForm,
    LocationForm,
  ],
  templateUrl: './edit-page.html',
  styleUrl: './edit-page.scss',
})
export class EditPage implements OnInit {
  private router = inject(Router);
  private labWorkService = inject(LabWorkService);
  private coordinatesService = inject(CoordinatesService);
  private disciplineService = inject(DisciplineService);
  private personService = inject(PersonService);
  private locationService = inject(LocationService);
  private browserLocation = inject(BrowserLocation);

  entity = input.required<string>();
  title = input.required<string>();
  id = input.required({ transform: numberAttribute });

  object = signal<any>(null);
  strategy: Strategy = 'pass';
  strategies = [
    { label: 'Pass', value: 'pass' },
    { label: 'Delete', value: 'delete' },
  ];

  back() {
    this.browserLocation.back();
  }

  ngOnInit() {
    const id = this.id();

    if (this.entity() === 'lab-work') {
      this.labWorkService.getLabWorkById(id).subscribe((o) => this.object.set(o));
    } else if (this.entity() === 'coordinates') {
      this.coordinatesService.getCoordinatesById(id).subscribe((o) => this.object.set(o));
    } else if (this.entity() === 'discipline') {
      this.disciplineService.getDisciplineById(id).subscribe((o) => this.object.set(o));
    } else if (this.entity() === 'person') {
      this.personService.getPersonById(id).subscribe((o) => this.object.set(o));
    } else {
      this.locationService.getLocationById(id).subscribe((o) => this.object.set(o));
    }
  }

  hasStrategy() {
    return this.entity() === 'lab-work' || this.entity() === 'person';
  }

  updateLabWork(lab: LabWork) {
    this.send(this.labWorkService.updateLabWork(this.id(), toLabWorkRequest(lab), this.strategy));
  }

  updateCoordinates(c: Coordinates) {
    this.send(this.coordinatesService.updateCoordinates(this.id(), c));
  }

  updateDiscipline(d: Discipline) {
    this.send(this.disciplineService.updateDiscipline(this.id(), d));
  }

  updatePerson(p: Person) {
    this.send(this.personService.updatePerson(this.id(), toPersonRequest(p), this.strategy));
  }

  updateLocation(l: Location) {
    this.send(this.locationService.updateLocation(this.id(), l));
  }


  private send(request: Observable<void>) {
    request.subscribe({
      next: () => this.back(),
      error: (e) => alert(e.error?.message ?? 'Не удалось сохранить'),
    });
  }
}
