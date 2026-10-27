import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { InputText } from 'primeng/inputtext';
import { Password } from 'primeng/password';
import { Button } from 'primeng/button';
import { Message } from 'primeng/message';
import { AuthService } from '../../service/AuthService';

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

  login() {
    this.authService.login({ username: this.username, password: this.password }).subscribe({
      next: () => this.router.navigate(['/labworks']),
      error: () => this.error.set('Неверный логин или пароль'),
    });
  }

  register() {
    this.authService.register({ username: this.username, password: this.password }).subscribe({
      next: () => this.router.navigate(['/labworks']),
      error: () => this.error.set('Не удалось зарегистрироваться'),
    });
  }
}
