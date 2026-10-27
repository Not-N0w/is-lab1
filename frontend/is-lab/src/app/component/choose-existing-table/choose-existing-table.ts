import { Component, inject, input, model, output, signal } from '@angular/core';
import { Observable } from 'rxjs';
import { Button } from 'primeng/button';
import { Dialog } from 'primeng/dialog';
import { TableModule } from 'primeng/table';
import { PersonService } from '../../service/PersonService';
import { CoordinatesService } from '../../service/CoordinatesService';
import { LocationService } from '../../service/LocationService';
import { DisciplineService } from '../../service/DisciplineService';

export type ChooseType = 'person' | 'coordinates' | 'location' | 'discipline';

@Component({
  selector: 'app-choose-existing-table',
  imports: [Button, Dialog, TableModule],
  templateUrl: './choose-existing-table.html',
  styleUrl: './choose-existing-table.scss',
})
export class ChooseExistingTable {
  private personService = inject(PersonService);
  private coordinatesService = inject(CoordinatesService);
  private locationService = inject(LocationService);
  private disciplineService = inject(DisciplineService);

  type = input.required<ChooseType>();
  visible = model(false);
  selected = output<any>();

  protected columns: string[] = [];
  protected objects = signal<any[]>([]);

  protected load() {
    let source: Observable<any[]>;

    switch (this.type()) {
      case 'person':
        source = this.personService.getAllPersons();
        this.columns = ['id', 'name', 'eyeColor', 'hairColor', 'height', 'nationality', 'location'];
        break;
      case 'coordinates':
        source = this.coordinatesService.getAllCoordinates();
        this.columns = ['id', 'x', 'y'];
        break;
      case 'location':
        source = this.locationService.getAllLocations();
        this.columns = ['id', 'name', 'x', 'y', 'z'];
        break;
      case 'discipline':
        source = this.disciplineService.getAllDisciplines();
        this.columns = ['id', 'name', 'practiceHours'];
        break;
    }

    source.subscribe((objects) => this.objects.set(objects));
  }

  protected field(col: string) {
    return col === 'location' ? 'location.name' : col;
  }

  protected choose(object: any) {
    this.selected.emit(object);
    this.visible.set(false);
  }
}
