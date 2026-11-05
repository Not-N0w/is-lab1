import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { InputText } from 'primeng/inputtext';
import { Password } from 'primeng/password';
import { Button } from 'primeng/button';
import { Message } from 'primeng/message';
import { AuthService } from '../../service/AuthService';
import { serverErrorMessage } from '../../service/error.service';

@Component({
  selector: 'app-auth-page',
  imports: [FormsModule, InputText, Password, Button, Message],
  templateUrl: './auth-page.html',
  styleUrl: './auth-page.scss',
})
export class AuthPage {
  private authService = inject(AuthService);
  private router = inject(Router);

  username = '';
  password = '';
  error = signal('');
  loading = signal(false);

  canSubmit() {
    return this.username.trim() !== '' && this.password !== '' && !this.loading();
  }

  login() {
    this.send(this.authService.login({ username: this.username.trim(), password: this.password }));
  }

  register() {
    this.send(this.authService.register({ username: this.username.trim(), password: this.password }));
  }

  private send(request: Observable<unknown>) {
    this.error.set('');
    this.loading.set(true);
    request.subscribe({
      next: () => {
        this.loading.set(false);
        this.router.navigate(['/labworks']);
      },
      error: (e) => {
        this.loading.set(false);
        this.error.set(serverErrorMessage(e));
      },
    });
  }
}
