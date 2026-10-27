import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Button } from 'primeng/button';
import { Select } from 'primeng/select';
import { Location, Person, toPersonRequest } from '../../model/Model';
import { LocationService } from '../../service/LocationService';
import { PersonService } from '../../service/PersonService';

@Component({
  selector: 'app-delete-location',
  imports: [FormsModule, Button, Select],
  templateUrl: './delete-location.html',
  styleUrl: '../delete.scss',
})
export class DeleteLocation implements OnInit {
  private locationService = inject(LocationService);
  private personService = inject(PersonService);

  target = input.required<Location>();
  deleted = output();
  cancel = output();

  related = signal<Person[]>([]);
  options = signal<any[]>([]);
  replacements: any = {};

  ngOnInit() {
    const id = this.target().id;

    this.personService.getByLocation(id!).subscribe((persons) => this.related.set(persons));

    this.locationService.getAllLocations().subscribe((list) => {
      const others = list.filter((l) => l.id !== id);
      this.options.set(
        others.map((l) => ({
          id: l.id,
          label: `#${l.id} ${l.name ?? ''} (${l.x}, ${l.y}, ${l.z})`,
        })),
      );
    });
  }

  canDelete() {
    return this.related().every((p) => this.replacements[p.id!]);
  }

  confirm() {
    const persons = this.related();
    if (persons.length === 0) {
      this.deleteTarget();
      return;
    }

    let updated = 0;
    for (const p of persons) {
      const request = toPersonRequest(p);
      request.locationId = this.replacements[p.id!];

      this.personService.updatePerson(p.id!, request, 'pass').subscribe(() => {
        updated++;
        if (updated === persons.length) this.deleteTarget();
      });
    }
  }

  private deleteTarget() {
    this.locationService.deleteLocation(this.target().id!).subscribe({
      next: () => this.deleted.emit(),
      error: (e) => alert(e.error?.message ?? 'Не удалось удалить'),
    });
  }
}
