import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Button } from 'primeng/button';
import { Select } from 'primeng/select';
import { Coordinates, LabWork, toLabWorkRequest } from '../../model/Model';
import { CoordinatesService } from '../../service/CoordinatesService';
import { LabWorkService } from '../../service/LabWorkService';

@Component({
  selector: 'app-delete-coordinates',
  imports: [FormsModule, Button, Select],
  templateUrl: './delete-coordinates.html',
  styleUrl: '../delete.scss',
})
export class DeleteCoordinates implements OnInit {
  private coordinatesService = inject(CoordinatesService);
  private labWorkService = inject(LabWorkService);

  target = input.required<Coordinates>();
  deleted = output();
  cancel = output();

  related = signal<LabWork[]>([]);
  options = signal<any[]>([]);
  replacements: any = {};

  ngOnInit() {
    const id = this.target().id;

    this.labWorkService.getByCoordinates(id!).subscribe((labs) => this.related.set(labs));

    this.coordinatesService.getAllCoordinates().subscribe((list) => {
      const others = list.filter((c) => c.id !== id);
      this.options.set(others.map((c) => ({ id: c.id, label: `#${c.id} (${c.x}, ${c.y})` })));
    });
  }

  canDelete() {
    return this.related().every((lab) => this.replacements[lab.id!]);
  }

  confirm() {
    const labs = this.related();
    if (labs.length === 0) {
      this.deleteTarget();
      return;
    }

    let updated = 0;
    for (const lab of labs) {
      const request = toLabWorkRequest(lab);
      request.coordinatesId = this.replacements[lab.id!];

      this.labWorkService.updateLabWork(lab.id!, request, 'pass').subscribe(() => {
        updated++;
        if (updated === labs.length) this.deleteTarget();
      });
    }
  }

  private deleteTarget() {
    this.coordinatesService.deleteCoordinates(this.target().id!).subscribe({
      next: () => this.deleted.emit(),
      error: (e) => alert(e.error?.message ?? 'Не удалось удалить'),
    });
  }
}
