import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../environment';
import { AuthRequest, AuthResponse } from '../model/Model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/auth/public`;

  username = signal<string | null>(null);

  constructor() {
    if (this.isAuthenticated()) this.loadName();
  }

  login(request: AuthRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.url}/login`, request)
      .pipe(tap((res) => this.saveTokens(res)));
  }

  register(request: AuthRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.url}/register`, request)
      .pipe(tap((res) => this.saveTokens(res)));
  }

  refresh(): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.url}/refresh`, { refreshToken: this.getRefreshToken() })
      .pipe(tap((res) => this.saveTokens(res)));
  }

  loadName() {
    this.http
      .get(`${environment.apiUrl}/auth/name`, { responseType: 'text' })
      .subscribe((name) => this.username.set(name));
  }

  logout(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    this.username.set(null);
  }

  isAuthenticated(): boolean {
    return this.getAccessToken() !== null;
  }

  getAccessToken(): string | null {
    return localStorage.getItem('accessToken');
  }

  getRefreshToken(): string | null {
    return localStorage.getItem('refreshToken');
  }

  private saveTokens(res: AuthResponse): void {
    localStorage.setItem('accessToken', res.accessToken);
    localStorage.setItem('refreshToken', res.refreshToken);
    this.loadName();
  }
}
