import { Component, inject, input } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Button } from 'primeng/button';
import {
  Coordinates,
  Discipline,
  LabWork,
  Location,
  Person,
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
  selector: 'app-create-page',
  imports: [Button, LabWorkForm, CoordinatesForm, DisciplineForm, PersonForm, LocationForm],
  templateUrl: './create-page.html',
  styleUrl: './create-page.scss',
})
export class CreatePage {
  private router = inject(Router);
  private labWorkService = inject(LabWorkService);
  private coordinatesService = inject(CoordinatesService);
  private disciplineService = inject(DisciplineService);
  private personService = inject(PersonService);
  private locationService = inject(LocationService);
  private browserLocation = inject(BrowserLocation);

  entity = input.required<string>();
  title = input.required<string>();

  createLabWork(lab: LabWork) {
    this.send(this.labWorkService.createLabWork(toLabWorkRequest(lab)));
  }

  createCoordinates(c: Coordinates) {
    this.send(this.coordinatesService.createCoordinates(c));
  }

  createDiscipline(d: Discipline) {
    this.send(this.disciplineService.createDiscipline(d));
  }

  createPerson(p: Person) {
    this.send(this.personService.createPerson(toPersonRequest(p)));
  }

  createLocation(l: Location) {
    this.send(this.locationService.createLocation(l));
  }

  back() {
    this.browserLocation.back();
  }

  private send(request: Observable<void>) {
    request.subscribe({
      next: () => this.back(),
      error: (e) => alert(e.error?.message ?? 'Не удалось создать'),
    });
  }
}
