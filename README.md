# DUQueue — Backend

Бэкенд системы электронной очереди Университета Дулати.

**Стек:** Spring Boot 3.5.3 · Java 17 · PostgreSQL 15 · Redis · WebSocket (STOMP)

---

## Быстрый старт

### Требования
- Java 17+
- Docker & Docker Compose

### Запуск

```bash
# 1. Запустить PostgreSQL + Redis
docker-compose up -d

# 2. Собрать и запустить (dev-профиль)
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## Тестовые аккаунты

> Тестовые данные применяются автоматически при первом старте через Liquibase
> (файл `db/changelog/postgres/20260311-001-insert-test-data.yaml`).
>
> ⚠️ Перед деплоем в **production** удали этот файл или ограничь через `context: dev`.

### Администратор

| Поле      | Значение              |
|-----------|-----------------------|
| Email     | `admin@dulaty.kz`     |
| Пароль    | `Admin123`            |
| Роль      | `ADMIN`               |
| Кабинет   | `/admin`              |

Имеет доступ к управлению отделами и менеджерами.

---

### Модератор

| Поле      | Значение              |
|-----------|-----------------------|
| Email     | `moder@dulaty.kz`     |
| Пароль    | `Moder123`            |
| Роль      | `MODERATOR`           |

Может добавлять/редактировать отделы и менеджеров, но без доступа к системным настройкам.

---

### Менеджеры очереди

Все менеджеры входят через кабинет `/manager`.
Пароль у всех одинаковый: **`Manager1`**

| Email                  | Имя                | Отдел        | Окно |
|------------------------|--------------------|--------------|------|
| `manager1@dulaty.kz`   | Нурсулу Ахметова   | Регистратура | 1    |
| `manager2@dulaty.kz`   | Дамир Сейткали     | Деканат      | 2    |
| `manager3@dulaty.kz`   | Зарина Байжанова   | Бухгалтерия  | 3    |
| `manager4@dulaty.kz`   | Алибек Нурланов    | Библиотека   | 4    |

---

### Тестовые отделы

| Префикс | Название       | Полное название                               |
|---------|----------------|-----------------------------------------------|
| `A`     | Регистратура   | Отдел регистрации и учёта студентов           |
| `B`     | Деканат        | Деканат по учебной и воспитательной работе    |
| `C`     | Бухгалтерия    | Финансово-экономический отдел                 |
| `D`     | Библиотека     | Библиотечный информационный центр             |

---

## API

| Метод  | Путь                                    | Доступ              | Описание                        |
|--------|-----------------------------------------|---------------------|---------------------------------|
| POST   | `/api/v1/auth/sign-in`                  | Public              | Вход                            |
| POST   | `/api/v1/auth/sign-up`                  | Public              | Регистрация                     |
| POST   | `/api/v1/auth/sign-out`                 | Authenticated       | Выход                           |
| GET    | `/api/v1/auth/user-info`                | Authenticated       | Данные текущего пользователя    |
| GET    | `/api/v1/queue/department`              | Public              | Список отделов                  |
| POST   | `/api/v1/queue/department`              | ADMIN, MODERATOR    | Создать отдел                   |
| PUT    | `/api/v1/queue/department/{id}`         | ADMIN, MODERATOR    | Обновить отдел                  |
| DELETE | `/api/v1/queue/department/{id}`         | ADMIN, MODERATOR    | Удалить отдел                   |
| GET    | `/api/v1/queue/manager`                 | Public              | Список менеджеров               |
| POST   | `/api/v1/queue/manager`                 | ADMIN, MODERATOR    | Добавить менеджера              |
| PUT    | `/api/v1/queue/manager/{id}`            | ADMIN, MODERATOR    | Обновить менеджера              |
| DELETE | `/api/v1/queue/manager/{id}`            | ADMIN, MODERATOR    | Удалить менеджера               |
| GET    | `/api/v1/queue/manager/activate/{id}`   | ADMIN, MODERATOR    | Активировать менеджера          |
| GET    | `/api/v1/queue/manager/deactivate/{id}` | ADMIN, MODERATOR    | Деактивировать менеджера        |
| POST   | `/api/v1/queue/ticket/generate`         | Public              | Получить талон                  |
| POST   | `/api/v1/queue/ticket/call-next`        | MANAGER             | Вызвать следующий талон         |
| GET    | `/api/v1/queue/ticket/active-tickets`   | Public              | Активные талоны (WAITING+CALLED)|
| GET    | `/api/v1/queue/ticket/manager-tickets`  | MANAGER             | Талоны текущего менеджера       |

---

## WebSocket

| Endpoint     | Протокол         | Описание              |
|--------------|------------------|-----------------------|
| `/ws`        | Native WebSocket | Основное подключение  |
| `/ws-sockjs` | SockJS           | Fallback для браузеров |

**Топики:**

| Топик                   | Формат сообщения     | Событие                        |
|-------------------------|----------------------|--------------------------------|
| `/topic/queue-events`   | `WsEventDto`         | Все события очереди            |
| `/topic/ticket-called`  | `TicketDto`          | Вызов талона (legacy)          |

**Типы событий `WsEventDto.eventType`:**

| Тип                | Когда отправляется                  |
|--------------------|-------------------------------------|
| `TICKET_GENERATED` | Новый талон создан (статус WAITING) |
| `TICKET_CALLED`    | Менеджер вызвал талон               |
| `TICKET_DONE`      | Талон завершён                      |
| `QUEUE_RESET`      | Ночной сброс очереди (00:00)        |

---

## Профили

| Профиль | Описание                        |
|---------|---------------------------------|
| `dev`   | Локальная разработка            |
| `prod`  | Продакшен (queue.dulaty.edu.kz) |

---

## Структура проекта

```
src/main/java/kz/dulaty/queue/
├── core/
│   ├── configs/          # Security, WebSocket, ShedLock
│   ├── exception/        # GlobalExceptionHandler
│   ├── logging/          # AOP-логирование событий
│   └── properties/       # Email, Scheduling properties
└── feature/
    ├── auth/             # Аутентификация, пользователи, роли
    ├── department/       # Управление отделами
    ├── manager/          # Управление менеджерами
    └── ticket/           # Генерация и вызов талонов, WebSocket-события
```
