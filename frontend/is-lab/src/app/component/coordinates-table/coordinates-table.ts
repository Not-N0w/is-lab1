import { Component, inject, OnDestroy, OnInit, signal } from '@angular/core';
import { merge, Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { TableModule } from 'primeng/table';
import { MenuItem } from 'primeng/api';
import { Button } from 'primeng/button';
import { Menu } from 'primeng/menu';
import { Coordinates } from '../../model/Model';
import { CoordinatesService } from '../../service/CoordinatesService';
import { DeleteWindow } from '../delete-window/delete-window';
import { WebSocketService } from '../../service/WebSocketService';

type CoordinatesRow = Coordinates & { actions: MenuItem[] };

@Component({
  selector: 'app-coordinates-table',
  imports: [TableModule, Button, Menu, DeleteWindow],
  templateUrl: './coordinates-table.html',
  styleUrl: '../table.scss',
})
export class CoordinatesTable implements OnInit, OnDestroy {
  private coordinatesService = inject(CoordinatesService);
  private router = inject(Router);
  private webSocketService = inject(WebSocketService);
  private wsSubscription?: Subscription;

  protected coordinates = signal<CoordinatesRow[]>([]);
  protected deleteVisible = signal(false);
  protected deleteTarget = signal<Coordinates | null>(null);

  ngOnInit() {
    this.reload();
    this.wsSubscription = merge(
      this.webSocketService.watch('coordinates'),
    ).subscribe(() => this.reload());
  }

  ngOnDestroy() {
    this.wsSubscription?.unsubscribe();
  }

  reload() {
    this.coordinatesService.getAllCoordinates().subscribe((list) =>
      this.coordinates.set(list.map((c) => ({ ...c, actions: this.actionsFor(c) }))),
    );
  }

  private actionsFor(c: Coordinates): MenuItem[] {
    return [
      { label: 'Edit', icon: 'pi pi-pencil', command: () => this.router.navigate(['/coordinates', c.id, 'edit']) },
      { label: 'Info', icon: 'pi pi-directions', command: () => this.router.navigate(['/coordinates', c.id]) },
      { separator: true },
      { label: 'Delete', icon: 'pi pi-trash', command: () => this.openDelete(c) },
    ];
  }

  protected openDelete(c: Coordinates) {
    this.deleteTarget.set(c);
    this.deleteVisible.set(true);
  }
}
