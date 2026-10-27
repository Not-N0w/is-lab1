import { Component, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { FilterService, MenuItem } from 'primeng/api';
import { Menubar } from 'primeng/menubar';
import { Button } from 'primeng/button';
import { AuthService } from './service/AuthService';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Menubar, Button],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected authService = inject(AuthService);
  private router = inject(Router);

  constructor() {
    inject(FilterService).register(
      'exact',
      (value: unknown, filter: unknown) =>
        filter === null ||
        filter === undefined ||
        filter === '' ||
        String(value) === String(filter),
    );
  }

  protected items: MenuItem[] = [
    { label: 'Lab works', icon: 'pi pi-book', routerLink: '/lab-work' },
    { label: 'Disciplines', icon: 'pi pi-briefcase', routerLink: '/discipline' },
    { label: 'Coordinates', icon: 'pi pi-map-marker', routerLink: '/coordinates' },
    { label: 'Persons', icon: 'pi pi-users', routerLink: '/person' },
    { label: 'Locations', icon: 'pi pi-globe', routerLink: '/location' },
    { label: 'Special operations', icon: 'pi pi-cog', routerLink: '/special' },
  ];

  protected logout() {
    this.authService.logout();
    this.router.navigate(['/auth']);
  }
}
