import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Button } from 'primeng/button';
import { Select } from 'primeng/select';
import { SelectButton } from 'primeng/selectbutton';
import { LabWork, Person, Strategy, toLabWorkRequest } from '../../model/Model';
import { PersonService } from '../../service/PersonService';
import { LabWorkService } from '../../service/LabWorkService';

@Component({
  selector: 'app-delete-person',
  imports: [FormsModule, Button, Select, SelectButton],
  templateUrl: './delete-person.html',
  styleUrl: '../delete.scss',
})
export class DeletePerson implements OnInit {
  private personService = inject(PersonService);
  private labWorkService = inject(LabWorkService);

  target = input.required<Person>();
  deleted = output();
  cancel = output();

  related = signal<LabWork[]>([]);
  options = signal<any[]>([]);
  replacements: any = {};
  strategy: Strategy = 'pass';
  strategies = [
    { label: 'Pass', value: 'pass' },
    { label: 'Delete', value: 'delete' },
  ];

  ngOnInit() {
    const id = this.target().id;

    this.labWorkService.getByAuthor(id!).subscribe((labs) => this.related.set(labs));

    this.personService.getAllPersons().subscribe((list) => {
      const others = list.filter((p) => p.id !== id);
      this.options.set(others.map((p) => ({ id: p.id, label: `#${p.id} ${p.name}` })));
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
      request.authorId = this.replacements[lab.id!];

      this.labWorkService.updateLabWork(lab.id!, request, 'pass').subscribe(() => {
        updated++;
        if (updated === labs.length) this.deleteTarget();
      });
    }
  }

  private deleteTarget() {
    this.personService.deletePerson(this.target().id!, this.strategy).subscribe({
      next: () => this.deleted.emit(),
      error: (e) => alert(e.error?.message ?? 'Не удалось удалить'),
    });
  }
}
