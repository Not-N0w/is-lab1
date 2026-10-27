import { Component, inject, OnDestroy, OnInit, signal } from '@angular/core';
import { merge, Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { TableModule } from 'primeng/table';
import { MenuItem } from 'primeng/api';
import { Button } from 'primeng/button';
import { Menu } from 'primeng/menu';
import { Location } from '../../model/Model';
import { LocationService } from '../../service/LocationService';
import { DeleteWindow } from '../delete-window/delete-window';
import { WebSocketService } from '../../service/WebSocketService';

type LocationRow = Location & { actions: MenuItem[] };

@Component({
  selector: 'app-location-table',
  imports: [TableModule, Button, Menu, DeleteWindow],
  templateUrl: './location-table.html',
  styleUrl: '../table.scss',
})
export class LocationTable implements OnInit, OnDestroy {
  private locationService = inject(LocationService);
  private router = inject(Router);
  private webSocketService = inject(WebSocketService);
  private wsSubscription?: Subscription;

  protected locations = signal<LocationRow[]>([]);
  protected deleteVisible = signal(false);
  protected deleteTarget = signal<Location | null>(null);

  ngOnInit() {
    this.reload();
    this.wsSubscription = merge(
      this.webSocketService.watch('location'),
    ).subscribe(() => this.reload());
  }

  ngOnDestroy() {
    this.wsSubscription?.unsubscribe();
  }

  reload() {
    this.locationService.getAllLocations().subscribe((list) =>
      this.locations.set(list.map((l) => ({ ...l, actions: this.actionsFor(l) }))),
    );
  }

  private actionsFor(l: Location): MenuItem[] {
    return [
      { label: 'Edit', icon: 'pi pi-pencil', command: () => this.router.navigate(['/location', l.id, 'edit']) },
      { label: 'Info', icon: 'pi pi-directions', command: () => this.router.navigate(['/location', l.id]) },
      { separator: true },
      { label: 'Delete', icon: 'pi pi-trash', command: () => this.openDelete(l) },
    ];
  }

  protected openDelete(l: Location) {
    this.deleteTarget.set(l);
    this.deleteVisible.set(true);
  }
}
