import { Component, computed, inject } from '@angular/core';
import { Dialog } from 'primeng/dialog';
import { Button } from 'primeng/button';
import { ErrorService } from '../../service/error.service';

@Component({
  selector: 'app-error-dialog',
  imports: [Dialog, Button],
  templateUrl: './error-dialog.html',
  styleUrl: './error-dialog.scss',
})
export class ErrorDialog {
  protected errorService = inject(ErrorService);
  protected visible = computed(() => this.errorService.message() !== null);

  protected onVisibleChange(v: boolean) {
    if (!v) this.errorService.close();
  }
}
