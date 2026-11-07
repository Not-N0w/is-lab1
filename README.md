# is-lab1

Веб-приложение для управления лабораторными работами (`LabWork`) и связанными сущностями: координатами, дисциплинами, авторами и локациями. Изменения рассылаются всем клиентам в реальном времени через WebSocket.

## Стек

- **Backend:** Java 17, Spring 7 (без Spring Boot), Spring Security + JWT, Spring Data JPA, Hibernate, Tomcat 11
- **Frontend:** Angular, PrimeNG
- **БД:** PostgreSQL

## Структура

```
backend/is-lab1/    — серверная часть (Gradle, WAR)
frontend/is-lab1/   — клиентская часть (Angular)
env/backend.env     — переменные окружения бэкенда
```
