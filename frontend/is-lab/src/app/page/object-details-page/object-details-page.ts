import { Component, effect, inject, input, numberAttribute, signal, untracked } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { Observable } from 'rxjs';
import { Button } from 'primeng/button';
import { LabWorkService } from '../../service/LabWorkService';
import { CoordinatesService } from '../../service/CoordinatesService';
import { DisciplineService } from '../../service/DisciplineService';
import { PersonService } from '../../service/PersonService';
import { LocationService } from '../../service/LocationService';
import { LabWorkDetails } from '../../component/lab-work-details/lab-work-details';
import { CoordinatesDetails } from '../../component/coordinates-details/coordinates-details';
import { DisciplineDetails } from '../../component/discipline-details/discipline-details';
import { PersonDetails } from '../../component/person-details/person-details';
import { LocationDetails } from '../../component/location-details/location-details';
import { DeleteWindow } from '../../component/delete-window/delete-window';
import { Location as BrowserLocation } from '@angular/common';

@Component({
  selector: 'app-object-details-page',
  imports: [
    RouterLink,
    Button,
    LabWorkDetails,
    CoordinatesDetails,
    DisciplineDetails,
    PersonDetails,
    LocationDetails,
    DeleteWindow,
  ],
  templateUrl: './object-details-page.html',
  styleUrl: './object-details-page.scss',
})
export class ObjectDetailsPage {
  private router = inject(Router);
  private labWorkService = inject(LabWorkService);
  private coordinatesService = inject(CoordinatesService);
  private disciplineService = inject(DisciplineService);
  private personService = inject(PersonService);
  private locationService = inject(LocationService);
  private browserLocation = inject(BrowserLocation);

  protected back() {
    this.browserLocation.back();
  }
  entity = input.required<string>();
  title = input.required<string>();
  id = input.required({ transform: numberAttribute });

  protected object = signal<any>(null);
  protected deleteVisible = signal(false);

  constructor() {
    effect(() => {
      const entity = this.entity();
      const id = this.id();
      untracked(() => this.load(entity, id));
    });
  }

  protected afterDelete() {
    this.router.navigate(['/' + this.entity()]);
  }

  private load(entity: string, id: number) {
    this.object.set(null);

    let source: Observable<any>;
    switch (entity) {
      case 'lab-work':
        source = this.labWorkService.getLabWorkById(id);
        break;
      case 'coordinates':
        source = this.coordinatesService.getCoordinatesById(id);
        break;
      case 'discipline':
        source = this.disciplineService.getDisciplineById(id);
        break;
      case 'person':
        source = this.personService.getPersonById(id);
        break;
      default:
        source = this.locationService.getLocationById(id);
    }

    source.subscribe((object) => this.object.set(object));
  }
}
