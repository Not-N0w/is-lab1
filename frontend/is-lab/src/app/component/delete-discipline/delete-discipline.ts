import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Button } from 'primeng/button';
import { Select } from 'primeng/select';
import { Discipline, LabWork, toLabWorkRequest } from '../../model/Model';
import { DisciplineService } from '../../service/DisciplineService';
import { LabWorkService } from '../../service/LabWorkService';

@Component({
  selector: 'app-delete-discipline',
  imports: [FormsModule, Button, Select],
  templateUrl: './delete-discipline.html',
  styleUrl: '../delete.scss',
})
export class DeleteDiscipline implements OnInit {
  private disciplineService = inject(DisciplineService);
  private labWorkService = inject(LabWorkService);

  target = input.required<Discipline>();
  deleted = output();
  cancel = output();

  related = signal<LabWork[]>([]);
  options = signal<any[]>([]);
  replacements: any = {};

  ngOnInit() {
    const id = this.target().id;

    this.labWorkService.getByDiscipline(id!).subscribe((labs) => this.related.set(labs));

    this.disciplineService.getAllDisciplines().subscribe((list) => {
      const others = list.filter((d) => d.id !== id);
      this.options.set(others.map((d) => ({ id: d.id, label: `#${d.id} ${d.name}` })));
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
      request.disciplineId = this.replacements[lab.id!];

      this.labWorkService.updateLabWork(lab.id!, request, 'pass').subscribe(() => {
        updated++;
        if (updated === labs.length) this.deleteTarget();
      });
    }
  }

  private deleteTarget() {
    this.disciplineService.deleteDiscipline(this.target().id!).subscribe({
      next: () => this.deleted.emit(),
      error: (e) => alert(e.error?.message ?? 'Не удалось удалить'),
    });
  }
}
