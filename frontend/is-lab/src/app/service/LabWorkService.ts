import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environment';
import { LabWork, LabWorkRequest, Strategy } from '../model/Model';

@Injectable({ providedIn: 'root' })
export class LabWorkService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/lab-works`;

  getAllLabWorks() {
    return this.http.get<LabWork[]>(`${this.url}/all`);
  }

  getLabWorkById(id: number) {
    return this.http.get<LabWork>(`${this.url}/${id}`);
  }

  createLabWork(request: LabWorkRequest) {
    return this.http.post<void>(`${this.url}/create`, request);
  }

  updateLabWork(id: number, request: LabWorkRequest, strategy: Strategy) {
    return this.http.patch<void>(`${this.url}/update/${id}`, request, { params: { strategy } });
  }

  deleteLabWork(id: number, strategy: Strategy) {
    return this.http.delete<void>(`${this.url}/delete/${id}`, { params: { strategy } });
  }

  getByCoordinates(coordinatesId: number) {
    return this.http.get<LabWork[]>(`${this.url}/by-coordinates`, { params: { coordinatesId } });
  }

  getByDiscipline(disciplineId: number) {
    return this.http.get<LabWork[]>(`${this.url}/by-discipline`, { params: { disciplineId } });
  }

  getByAuthor(authorId: number) {
    return this.http.get<LabWork[]>(`${this.url}/by-author`, { params: { authorId } });
  }
}
