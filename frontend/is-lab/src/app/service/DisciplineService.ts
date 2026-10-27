import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environment';
import { Discipline, LabWork } from '../model/Model';

@Injectable({ providedIn: 'root' })
export class DisciplineService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/disciplines`;

  getAllDisciplines() {
    return this.http.get<Discipline[]>(`${this.url}/all`);
  }

  getDisciplineById(id: number) {
    return this.http.get<Discipline>(`${this.url}/${id}`);
  }

  getRelatedLabWorks(id: number) {
    return this.http.get<LabWork[]>(`${this.url}/${id}/lab-works`);
  }

  createDiscipline(discipline: Discipline) {
    return this.http.post<void>(`${this.url}/create`, discipline);
  }

  updateDiscipline(id: number, discipline: Discipline) {
    return this.http.patch<void>(`${this.url}/update/${id}`, discipline);
  }

  deleteDiscipline(id: number) {
    return this.http.delete<void>(`${this.url}/delete/${id}`);
  }
}
