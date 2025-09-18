# WebSockets Application

Приложение на основе WebSocket для обмена сообщениями в реальном времени с микросервисной архитектурой.

## 🏗 Архитектура

Приложение состоит из следующих компонентов:

### 🎨 Frontend
- React + TypeScript
- Vite для сборки
- Redux Toolkit для управления состоянием
- React Router для маршрутизации
- Axios для HTTP запросов
- Кастомные UI компоненты
- Поддержка тем (светлая/темная)

### 🔧 Backend Сервисы

#### Security Service (Порт: 8081)
- Аутентификация и авторизация
- JWT токены (access and refresh)
- PostgreSQL для хранения данных пользователей
- Spring Security

#### Mediator Service (Порт: 8080)
- Обработка WebSocket соединений
- Интеграция с Kafka для обмена сообщениями
- Spring WebSocket

#### Notification Service (Порт: 8082)
- Обработка и рассылка уведомлений
- WebSocket endpoint для клиентов
- Интеграция с Kafka

### 📡 Messaging
- Apache Kafka для асинхронного обмена сообщениями между сервисами

## 🚀 Запуск приложения

### Требования
- Docker и Docker Compose
- Node.js 18+ (для локальной разработки)
- Java 17+ (для локальной разработки)

### Переменные окружения
Создайте файл `.env` в корневой директории:

```env
# Общие
SERVER_PORT=8080

# Security Service
SECURITY_POSTGRES_DB=security_db
SECURITY_POSTGRES_USERNAME=security_user
SECURITY_POSTGRES_PASSWORD=security_password
SECURITY_POSTGRES_PORT=5432
SECURITY_POSTGRES_HOST=postgres-security

# JWT
JWT_SECRET=your_jwt_secret_key
JWT_ACCESS_EXPIRATION=3600
JWT_REFRESH_EXPIRATION=86400
```

### Docker Compose
1. Сборка и запуск всех сервисов:
```bash
  docker compose up --build
```

2. Остановка:
```bash
  docker compose down
```

### Локальная разработка

#### Frontend
```bash
  cd frontend
  npm install
  npm run dev
```

#### Backend сервисы
Каждый сервис можно запустить локально через Gradle:
```bash
  cd backend/[service-name]
  ./gradlew bootRun
```

## 📝 API Endpoints

### Security Service
- POST `/api/auth/register` - Регистрация
- POST `/api/auth/login` - Аутентификация
- POST `/api/auth/refresh` - Обновление токена

### WebSocket Endpoints
- Mediator Service: `ws://localhost:8080/ws`
- Notification Service: `ws://localhost:8082/ws/notifications`

## 🔐 Безопасность
- Все REST запросы защищены JWT токенами
- WebSocket соединения требуют валидный JWT токен
- Пароли хэшируются с использованием BCrypt
- CORS настроен для безопасного взаимодействия между сервисами

## 📦 База данных
PostgreSQL используется для хранения данных пользователей.

### Схема таблицы users

| Поле       | Тип данных   | Ограничения      | Описание                        |
|------------|--------------|------------------|---------------------------------|
| id         | BIGSERIAL    | PRIMARY KEY      | Уникальный идентификатор        |
| username   | VARCHAR(50)  | NOT NULL         | Имя пользователя                |
| email      | VARCHAR(100) | NOT NULL, UNIQUE | Электронная почта               |
| password   | TEXT         | NOT NULL         | Хэшированный пароль (BCrypt)    |
| role       | VARCHAR(50)  | NOT NULL         | Роль пользователя (USER, ADMIN) |
| created_at | DATE         | NOT NULL         | Дата создания аккаунта          |

**Индексы:**
- `PRIMARY KEY` на поле `id`
- `UNIQUE INDEX ux_users_email` на поле `email`

## 🛠 Технологический стек
- **Frontend**: React, TypeScript, Redux, Vite
- **Backend**: Java 17, Spring Boot, Spring Security, Spring WebSocket
- **База данных**: PostgreSQL
- **Messaging**: Apache Kafka
- **Контейнеризация**: Docker, Docker Compose
- **Безопасность**: JWT, BCrypt