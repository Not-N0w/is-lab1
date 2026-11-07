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

## Redis implementation

### Замеры

#### Количество строк:
- lab_work: 1120
- person: 896
- location: 896
- discipline: 1120
- coordinates: 1120

Смотрим на время ответа и пропускную способность
(сценарий JMeter: users: 100, ramp-up: 2 s, loops: 20, логин один раз на пользователя)

#### 1. Без кэша

| Запрос | Average | Median | Throughput | Error rate |
|---|---|---|---|---|
| login request | 996 мс | 1 094 мс | 31,94 req/s | 0 % |
| all_labs request | 1 423 мс | 1 414 мс | 48,61 req/s | 0 % |
| all_persons request | 408 мс | 394 мс | 48,75 req/s | 0 % |
| all_coordinates request | 22 мс | 13 мс | 48,82 req/s | 0 % |
| all_disciplines request | 20 мс | 13 мс | 48,83 req/s | 0 % |
| all_locations request | 20 мс | 13 мс | 48,85 req/s | 0 % |
| TOTAL | 385 мс | 33 мс | 244,72 req/s | 0 % |

#### 2. Кэш для всех сущностей

| Запрос | Average | Median | Throughput | Error rate |
|---|---|---|---|---|
| login request | 2 869 мс | 2 841 мс | 25,16 req/s | 0 % |
| all_labs request | 694 мс | 188 мс | 79,68 req/s | 0 % |
| all_persons request | 184 мс | 90 мс | 108,13 req/s | 0 % |
| all_coordinates request | 127 мс | 87 мс | 118,32 req/s | 0 % |
| all_disciplines request | 113 мс | 89 мс | 120,36 req/s | 0 % |
| all_locations request | 121 мс | 87 мс | 122,65 req/s | 0 % |
| TOTAL | 274 мс | 102 мс | 349,26 req/s | 0 % |

Изменение относительно варианта без кэша:

| Запрос | Average | Median | Throughput |
|---|---|---|---|
| all_labs | −51,2 % | −86,7 % | +63,9 % |
| all_persons | −54,9 % | −77,2 % | +121,8 % |
| all_coordinates | +477 % | +569 % | +142,3 % |
| all_disciplines | +465 % | +585 % | +146,5 % |
| all_locations | +505 % | +569 % | +151,1 % |
| **TOTAL** | **−28,8 %** | +209 % | **+42,7 %** |

#### 3. Кэш только для lab_work и person

| Запрос | Average | Median | Throughput | Error rate |
|---|---|---|---|---|
| login request | 840 мс | 898 мс | 33,13 req/s | 0 % |
| all_labs request | 205 мс | 181 мс | 202,65 req/s | 0 % |
| all_persons request | 60 мс | 35 мс | 202,80 req/s | 0 % |
| all_coordinates request | 34 мс | 26 мс | 202,86 req/s | 0 % |
| all_disciplines request | 36 мс | 27 мс | 202,90 req/s | 0 % |
| all_locations request | 36 мс | 27 мс | 202,92 req/s | 0 % |
| TOTAL | 82 мс | 30 мс | 1 014,06 req/s | 0 % |

Изменение относительно варианта без кэша:

| Запрос | Average | Median | Throughput |
|---|---|---|---|
| all_labs | −85,6 % | −87,2 % | +316,9 % |
| all_persons | −85,3 % | −91,1 % | +316,0 % |
| all_coordinates | +54,5 % | +100 % | +315,5 % |
| all_disciplines | +80,0 % | +107,7 % | +315,5 % |
| all_locations | +80,0 % | +107,7 % | +315,4 % |
| **TOTAL** | **−78,7 %** | −9,1 % | **+314,4 %** |

### Выводы

- Кэш ускоряет тяжёлые запросы со связями (`all_labs`, `all_persons`): время ответа −85 %.
- Для простых таблиц без связей кэш вредит: Redis и сериализация дороже, чем запрос к БД.
- Кэшировать только тяжёлые запросы оптимально: throughput ×4 (245 -> 1 014 req/s).

Итог: кэшируются только `all_labs` и `all_persons`.