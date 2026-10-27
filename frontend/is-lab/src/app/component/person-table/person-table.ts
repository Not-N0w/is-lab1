import { Component, inject, OnDestroy, OnInit, signal } from '@angular/core';
import { merge, Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { TableModule } from 'primeng/table';
import { MenuItem } from 'primeng/api';
import { Button } from 'primeng/button';
import { Menu } from 'primeng/menu';
import { Person } from '../../model/Model';
import { PersonService } from '../../service/PersonService';
import { DeleteWindow } from '../delete-window/delete-window';
import { WebSocketService } from '../../service/WebSocketService';

type PersonRow = Person & { actions: MenuItem[] };

@Component({
  selector: 'app-person-table',
  imports: [TableModule, Button, Menu, DeleteWindow],
  templateUrl: './person-table.html',
  styleUrl: '../table.scss',
})
export class PersonTable implements OnInit, OnDestroy {
  private personService = inject(PersonService);
  private router = inject(Router);
  private webSocketService = inject(WebSocketService);
  private wsSubscription?: Subscription;

  protected persons = signal<PersonRow[]>([]);
  protected deleteVisible = signal(false);
  protected deleteTarget = signal<Person | null>(null);

  ngOnInit() {
    this.reload();
    this.wsSubscription = merge(
      this.webSocketService.watch('person'),
      this.webSocketService.watch('location'),
    ).subscribe(() => this.reload());
  }

  ngOnDestroy() {
    this.wsSubscription?.unsubscribe();
  }

  reload() {
    this.personService.getAllPersons().subscribe((list) =>
      this.persons.set(list.map((p) => ({ ...p, actions: this.actionsFor(p) }))),
    );
  }

  private actionsFor(p: Person): MenuItem[] {
    return [
      { label: 'Edit', icon: 'pi pi-pencil', command: () => this.router.navigate(['/person', p.id, 'edit']) },
      { label: 'Info', icon: 'pi pi-directions', command: () => this.router.navigate(['/person', p.id]) },
      { separator: true },
      { label: 'Delete', icon: 'pi pi-trash', command: () => this.openDelete(p) },
    ];
  }

  protected openDelete(p: Person) {
    this.deleteTarget.set(p);
    this.deleteVisible.set(true);
  }
}
