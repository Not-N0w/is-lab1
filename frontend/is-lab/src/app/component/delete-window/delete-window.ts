import { Component, input, model, output } from '@angular/core';
import { Dialog } from 'primeng/dialog';
import { DeleteLabWork } from '../delete-lab-work/delete-lab-work';
import { DeleteCoordinates } from '../delete-coordinates/delete-coordinates';
import { DeleteDiscipline } from '../delete-discipline/delete-discipline';
import { DeleteLocation } from '../delete-location/delete-location';
import { DeletePerson } from '../delete-person/delete-person';

@Component({
  selector: 'app-delete-window',
  imports: [Dialog, DeleteLabWork, DeleteCoordinates, DeleteDiscipline, DeleteLocation, DeletePerson],
  templateUrl: './delete-window.html',
})
export class DeleteWindow {
  entity = input.required<string>();
  target = input<any>(null);
  visible = model(false);
  deleted = output<void>();

  protected done() {
    this.visible.set(false);
    this.deleted.emit();
  }
}
