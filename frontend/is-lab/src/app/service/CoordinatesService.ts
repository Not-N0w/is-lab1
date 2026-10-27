import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environment';
import { Coordinates, LabWork } from '../model/Model';

@Injectable({ providedIn: 'root' })
export class CoordinatesService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/coordinates`;

  getAllCoordinates() {
    return this.http.get<Coordinates[]>(`${this.url}/all`);
  }

  getCoordinatesById(id: number) {
    return this.http.get<Coordinates>(`${this.url}/${id}`);
  }

  getRelatedLabWorks(id: number) {
    return this.http.get<LabWork[]>(`${this.url}/${id}/lab-works`);
  }

  createCoordinates(coordinates: Coordinates) {
    return this.http.post<void>(`${this.url}/create`, coordinates);
  }

  updateCoordinates(id: number, coordinates: Coordinates) {
    return this.http.patch<void>(`${this.url}/update/${id}`, coordinates);
  }

  deleteCoordinates(id: number) {
    return this.http.delete<void>(`${this.url}/delete/${id}`);
  }
}
