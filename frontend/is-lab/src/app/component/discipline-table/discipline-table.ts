import { Component, inject, OnDestroy, OnInit, signal } from '@angular/core';
import { merge, Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { TableModule } from 'primeng/table';
import { MenuItem } from 'primeng/api';
import { Button } from 'primeng/button';
import { Menu } from 'primeng/menu';
import { Discipline } from '../../model/Model';
import { DisciplineService } from '../../service/DisciplineService';
import { DeleteWindow } from '../delete-window/delete-window';
import { WebSocketService } from '../../service/WebSocketService';

type DisciplineRow = Discipline & { actions: MenuItem[] };

@Component({
  selector: 'app-discipline-table',
  imports: [TableModule, Button, Menu, DeleteWindow],
  templateUrl: './discipline-table.html',
  styleUrl: '../table.scss',
})
export class DisciplineTable implements OnInit, OnDestroy {
  private disciplineService = inject(DisciplineService);
  private router = inject(Router);
  private webSocketService = inject(WebSocketService);
  private wsSubscription?: Subscription;

  protected disciplines = signal<DisciplineRow[]>([]);
  protected deleteVisible = signal(false);
  protected deleteTarget = signal<Discipline | null>(null);

  ngOnInit() {
    this.reload();
    this.wsSubscription = merge(
      this.webSocketService.watch('discipline'),
    ).subscribe(() => this.reload());
  }

  ngOnDestroy() {
    this.wsSubscription?.unsubscribe();
  }

  reload() {
    this.disciplineService.getAllDisciplines().subscribe((list) =>
      this.disciplines.set(list.map((d) => ({ ...d, actions: this.actionsFor(d) }))),
    );
  }

  private actionsFor(d: Discipline): MenuItem[] {
    return [
      { label: 'Edit', icon: 'pi pi-pencil', command: () => this.router.navigate(['/discipline', d.id, 'edit']) },
      { label: 'Info', icon: 'pi pi-directions', command: () => this.router.navigate(['/discipline', d.id]) },
      { separator: true },
      { label: 'Delete', icon: 'pi pi-trash', command: () => this.openDelete(d) },
    ];
  }

  protected openDelete(d: Discipline) {
    this.deleteTarget.set(d);
    this.deleteVisible.set(true);
  }
}
