import { Component, inject, OnDestroy, OnInit, signal } from '@angular/core';
import { merge, Subscription } from 'rxjs';
import { formatDate } from '@angular/common';
import { Router } from '@angular/router';
import { TableModule } from 'primeng/table';
import { FilterService, MenuItem } from 'primeng/api';
import { Tag } from 'primeng/tag';
import { Button } from 'primeng/button';
import { Menu } from 'primeng/menu';
import { LabWork } from '../../model/Model';
import { LabWorkService } from '../../service/LabWorkService';
import { DeleteWindow } from '../delete-window/delete-window';
import { WebSocketService } from '../../service/WebSocketService';

type LabWorkRow = LabWork & { creationDateText: string; actions: MenuItem[] };

@Component({
  selector: 'app-lab-work-table',
  imports: [TableModule, Tag, Button, Menu, DeleteWindow],
  templateUrl: './lab-work-table.html',
  styleUrl: '../table.scss',
})
export class LabWorkTable implements OnInit, OnDestroy {
  private labWorkService = inject(LabWorkService);
  private filterService = inject(FilterService);
  private router = inject(Router);
  private webSocketService = inject(WebSocketService);
  private wsSubscription?: Subscription;

  protected labWorks = signal<LabWorkRow[]>([]);
  protected deleteVisible = signal(false);
  protected deleteTarget = signal<LabWork | null>(null);


  ngOnInit() {
    this.reload();
    this.wsSubscription = merge(
      this.webSocketService.watch('lab-work'),
      this.webSocketService.watch('coordinates'),
      this.webSocketService.watch('discipline'),
      this.webSocketService.watch('person'),
    ).subscribe(() => this.reload());
  }

  ngOnDestroy() {
    this.wsSubscription?.unsubscribe();
  }

  reload() {
    this.labWorkService.getAllLabWorks().subscribe((labWorks) =>
      this.labWorks.set(
        labWorks.map((lab) => ({
          ...lab,
          creationDateText: formatDate(lab.creationDate, 'dd.MM.yyyy HH:mm:ss', 'en-US'),
          actions: this.actionsFor(lab),
        })),
      ),
    );
  }

  private actionsFor(lab: LabWork): MenuItem[] {
    return [
      { label: 'Edit', icon: 'pi pi-pencil', command: () => this.router.navigate(['/lab-work', lab.id, 'edit']) },
      { label: 'Info', icon: 'pi pi-directions', command: () => this.router.navigate(['/lab-work', lab.id]) },
      { separator: true },
      { label: 'Delete', icon: 'pi pi-trash', command: () => this.openDelete(lab) },
    ];
  }

  protected openDelete(lab: LabWork) {
    this.deleteTarget.set(lab);
    this.deleteVisible.set(true);
  }

  protected difficultyTagSeverity(difficulty: string): 'success' | 'warn' | 'danger' | 'info' {
    switch (difficulty) {
      case 'VERY_EASY':
        return 'success';
      case 'HARD':
        return 'warn';
      case 'TERRIBLE':
        return 'danger';
      default:
        return 'info';
    }
  }
}
