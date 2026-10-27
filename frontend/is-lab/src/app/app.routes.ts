import { inject } from '@angular/core';
import { Router, Routes } from '@angular/router';
import { AuthService } from './service/AuthService';
import { AuthPage } from './page/auth-page/auth-page';
import { TablePage } from './page/table-page/table-page';
import { CreatePage } from './page/create-page/create-page';
import { ObjectDetailsPage } from './page/object-details-page/object-details-page';
import { SpecialFunctionsPage } from './page/special-functions-page/special-functions-page';
import { EditPage } from './page/edit-page/edit-page';

const authGuard = () =>
  inject(AuthService).isAuthenticated() || inject(Router).createUrlTree(['/auth']);

const entityRoutes = (entity: string, plural: string, single: string): Routes => [
  { path: entity, component: TablePage, data: { entity, title: plural } },
  {
    path: `${entity}/create`,
    component: CreatePage,
    data: { entity, title: `New ${single.toLowerCase()}` },
  },
  { path: `${entity}/:id`, component: ObjectDetailsPage, data: { entity, title: single } },
  {
    path: `${entity}/:id/edit`,
    component: EditPage,
    data: { entity, title: `Edit ${single.toLowerCase()}` },
  },
];

export const routes: Routes = [
  { path: 'auth', component: AuthPage },
  {
    path: '',
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'lab-work', pathMatch: 'full' },
      ...entityRoutes('lab-work', 'Lab works', 'Lab work'),
      ...entityRoutes('discipline', 'Disciplines', 'Discipline'),
      ...entityRoutes('coordinates', 'Coordinates', 'Coordinates'),
      ...entityRoutes('person', 'Persons', 'Person'),
      ...entityRoutes('location', 'Locations', 'Location'),
      { path: 'special', component: SpecialFunctionsPage },
    ],
  },
  { path: '**', redirectTo: '' },
];
