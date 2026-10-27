import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environment';
import { Location, Person } from '../model/Model';

@Injectable({ providedIn: 'root' })
export class LocationService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/locations`;

  getAllLocations() {
    return this.http.get<Location[]>(`${this.url}/all`);
  }

  getLocationById(id: number) {
    return this.http.get<Location>(`${this.url}/${id}`);
  }

  getRelatedPersons(id: number) {
    return this.http.get<Person[]>(`${this.url}/${id}/persons`);
  }

  createLocation(location: Location) {
    return this.http.post<void>(`${this.url}/create`, location);
  }

  updateLocation(id: number, location: Location) {
    return this.http.patch<void>(`${this.url}/update/${id}`, location);
  }

  deleteLocation(id: number) {
    return this.http.delete<void>(`${this.url}/delete/${id}`);
  }
}
