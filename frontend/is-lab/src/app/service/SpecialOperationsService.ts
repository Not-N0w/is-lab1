import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environment';
import { Person } from '../model/Model';

export interface MinimalPointCount {
  minimalPoints: number;
  count: number;
}

@Injectable({ providedIn: 'root' })
export class SpecialOperationsService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/lab-works/special`;

  getMinimalPointSum() {
    return this.http.get<number>(`${this.url}/sum-of-minimal-points`);
  }

  countByMinimalPoint() {
    return this.http.get<MinimalPointCount[]>(`${this.url}/get-labs-grouped-by-minimal-points`);
  }

  getUniqueAuthors() {
    return this.http.get<number[]>(`${this.url}/get-unique-authors-ids`);
  }

  addLabToDiscipline(labId: number, disciplineId: number) {
    return this.http.post<void>(`${this.url}/add-lab-to-discipline`, null, {
      params: { labId, disciplineId },
    });
  }

  addTopLabsToDiscipline(disciplineId: number) {
    return this.http.post<void>(`${this.url}/add-top-labs-to-discipline`, null, {
      params: { disciplineId },
    });
  }
}
