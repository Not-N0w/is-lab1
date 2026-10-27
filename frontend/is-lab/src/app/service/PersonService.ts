import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environment';
import { LabWork, Person, PersonRequest, Strategy } from '../model/Model';

@Injectable({ providedIn: 'root' })
export class PersonService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/persons`;

  getAllPersons() {
    return this.http.get<Person[]>(`${this.url}/all`);
  }

  getPersonById(id: number) {
    return this.http.get<Person>(`${this.url}/${id}`);
  }

  createPerson(request: PersonRequest) {
    return this.http.post<void>(`${this.url}/create`, request);
  }

  updatePerson(id: number, request: PersonRequest, strategy: Strategy) {
    return this.http.patch<void>(`${this.url}/update/${id}`, request, { params: { strategy } });
  }

  deletePerson(id: number, strategy: Strategy) {
    return this.http.delete<void>(`${this.url}/delete/${id}`, { params: { strategy } });
  }

  getByLocation(locationId: number) {
    return this.http.get<Person[]>(`${this.url}/by-location`, { params: { locationId } });
  }
}
