import { Injectable, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

function messageFromBody(body: any): string | null {
  if (typeof body === 'string') {
    if (!body.trim()) return null;
    try {
      return messageFromBody(JSON.parse(body)) ?? body;
    } catch {
      return body;
    }
  }
  if (body && typeof body === 'object') {
    if (Array.isArray(body.errors) && body.errors.length) {
      return body.errors
        .map((x: any) =>
          typeof x === 'string' ? x : (x.defaultMessage ?? x.message ?? JSON.stringify(x)),
        )
        .join('\n');
    }
    if (typeof body.message === 'string' && body.message) return body.message;
    if (typeof body.error === 'string' && body.error) return body.error;
    if (typeof body.detail === 'string' && body.detail) return body.detail;
  }
  return null;
}

export function serverErrorMessage(e: unknown): string {
  if (!(e instanceof HttpErrorResponse)) {
    return e instanceof Error ? e.message : 'Неизвестная ошибка';
  }
  if (e.status === 0) {
    return 'Сервер недоступен';
  }
  return (
    messageFromBody(e.error) ??
    (e.statusText && e.statusText !== 'OK' ? `${e.status} ${e.statusText}` : `Ошибка ${e.status}`)
  );
}

@Injectable({ providedIn: 'root' })
export class ErrorService {
  readonly message = signal<string | null>(null);

  show(message: string) {
    this.message.set(message);
  }

  showError(e: unknown) {
    this.show(serverErrorMessage(e));
  }

  close() {
    this.message.set(null);
  }
}
