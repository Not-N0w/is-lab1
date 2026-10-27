import { Component, inject, input, output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Button } from 'primeng/button';
import { SelectButton } from 'primeng/selectbutton';
import { LabWork, Strategy } from '../../model/Model';
import { LabWorkService } from '../../service/LabWorkService';

@Component({
  selector: 'app-delete-lab-work',
  imports: [FormsModule, Button, SelectButton],
  templateUrl: './delete-lab-work.html',
  styleUrl: '../delete.scss',
})
export class DeleteLabWork {
  private labWorkService = inject(LabWorkService);

  target = input.required<LabWork>();
  deleted = output<void>();
  cancel = output<void>();

  protected strategy: Strategy = 'pass';
  protected strategies = [
    { label: 'Pass', value: 'pass' },
    { label: 'Delete', value: 'delete' },
  ];

  protected confirm() {
    this.labWorkService.deleteLabWork(this.target().id!, this.strategy).subscribe({
      next: () => this.deleted.emit(),
      error: (e) => alert(e.error?.message ?? 'Не удалось удалить'),
    });
  }
}
