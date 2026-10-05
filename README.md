# SmartSplit

SmartSplit is a Splitwise-alternative REST API for splitting shared expenses between friends, built as a learning project to deepen expertise in **Java, Spring Boot, Docker and Kafka**.

## Tech stack

| Area | Technology |
|------|------------|
| Language | Java 21 |
| Framework | Spring Boot 4.1 (Web MVC, Data JPA, Security, Validation, Actuator) |
| Database | PostgreSQL 17 + Flyway migrations |
| Messaging | Apache Kafka 3.9 (KRaft mode) via Spring Kafka |
| Mapping | MapStruct |
| Boilerplate | Lombok |
| API docs | springdoc-openapi (Swagger UI) |
| Build | Maven (wrapper included) |
| Infra | Docker Compose |

Testcontainers is planned but not yet integrated.

## Getting started

### Prerequisites

- JDK 21
- Docker (with Docker Compose)
- A [currencyapi.net](https://currencyapi.net) API key

### 1. Configure environment

Create a `.env` file in the project root (it is loaded by `spring-dotenv`):

```properties
CURRENCY_API_KEY=your_api_key_here
```

### 2. Start the infrastructure

```bash
docker-compose up -d
```

This starts:

- **PostgreSQL** on `localhost:5432` (db `smartsplit`, user/password `postgres`)
- **Kafka** (single node, KRaft, no ZooKeeper) on `localhost:9092`

### 3. Run the app

```bash
./mvnw spring-boot:run
```

Flyway applies the migrations in `src/main/resources/db/migration` on startup. The API is served on `http://localhost:8080`.

Swagger UI: <http://localhost:8080/swagger-ui.html>

### Build and test

```bash
./mvnw compile
./mvnw test
./mvnw test -Dtest=EqualExpenseCalculatorTest   # a single class
```

## API overview

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/users` | Register a user (public) |
| `GET` | `/users` | List users |
| `POST` | `/groups` | Create a group (publishes `group.created`) |
| `GET` | `/groups` | List groups |
| `PUT` | `/groups/members` | Add members to a group |
| `DELETE` | `/groups/members` | Remove members from a group |
| `POST` | `/expenses/equalExpense` | Create an expense split equally between members |

Everything except Swagger and `POST /users` requires authentication, but no authentication mechanism (e.g. JWT) is wired up yet. See the Swagger UI for request and response schemas.

## Architecture

The code follows a **layered (package-by-layer)** structure under `pt.saraborges.smartsplit`:

```
controller/   REST controllers, one per domain
service/      business logic (@Service, constructor injection)
repository/   Spring Data JPA repositories
entity/       JPA entities, one subpackage per domain (+ valueobject/)
dto/          request/response DTOs as Java records
mapper/       MapStruct mappers
validator/    standalone validators (not Bean Validation)
event/        Kafka event records, topic names and publishers
config/       Spring @Configuration classes
exception/    exception hierarchy + global @RestControllerAdvice
```

Key conventions:

- **Exceptions**: domain failures throw a `BaseException` subclass (`ValidationException` 400, `ConflictException` 409, `BusinessRuleException` 422, …). `GlobalExceptionHandler` converts them to an `ErrorResponse`.
- **Value objects**: `Email` and `Password` are built through static factories that validate first, so invalid instances can't exist.
- **Validators** are registered as beans in `config/ValidatorConfig`. Don't add `@Component` to them.
- **MapStruct**: `mapstruct-processor` must stay in the `maven-compiler-plugin` `annotationProcessorPaths` in `pom.xml`, otherwise the mapper beans are never generated.

## Event-driven messaging (Kafka)

SmartSplit publishes domain events to Kafka so other services can react without being coupled to this API.

| Topic | Key | Payload | Published when |
|-------|-----|---------|----------------|
| `group.created` | group id | `GroupCreatedEvent(groupId, name, createdByUserId, occurredAt)` | A group is created |

Events are published by `GroupEventPublisher` using `@TransactionalEventListener(phase = AFTER_COMMIT)`. A message is only sent after the database transaction has committed, so consumers never see events for rolled-back data. Values are serialized as JSON with Spring's `JsonSerializer`.

Topic names live in `event/KafkaTopics.java`.

## Notifications service (Kafka consumers)

The consuming side lives in a separate project:

**[SmartSplitNotifications](https://github.com/borges-sara/SmartSplitNotifications)** (local path: `../SmartSplitNotifications`)

It is a standalone Spring Boot app that consumes the events published here and is meant to turn them into user notifications (e-mail via `spring-boot-starter-mail`).

```
┌──────────────┐   group.created    ┌───────────────┐   ┌──────────────────────────┐
│  SmartSplit  │ ─────────────────▶ │     Kafka     │ ─▶│  SmartSplitNotifications │
│  (producer)  │   (JSON, key=id)   │ localhost:9092│   │  group: notification-    │
└──────────────┘                    └───────────────┘   │  service (consumer)      │
                                                        └──────────────────────────┘
```

| | SmartSplit (this repo) | SmartSplitNotifications |
|---|---|---|
| Role | REST API, Kafka **producer** | Kafka **consumer** |
| Java | 21 | 17 |
| Broker | `localhost:9092` | `localhost:9092` |
| Consumer group | n/a | `notification-service` |

### Running both together

1. Start the broker from this repo: `docker-compose up -d`
2. Start SmartSplit: `./mvnw spring-boot:run`
3. In the notifications repo, start the consumer: `./mvnw spring-boot:run`

Both services point at the same broker, so make sure it is running before starting either one.

### Current status

The notifications service is in an early stage:

- Consumer configuration is in place (`KafkaConsumerConfig`, group `notification-service`, `auto-offset-reset=earliest`).
- Domain model for notifications exists (`Notification`, `NotificationType`, `NotificationChannel`, `NotificationStatus`).
- The only active listener is a demo one on a `greetings` topic.
- **Still to do**: a listener for `group.created`, sending e-mails, and agreeing on a shared event contract. The consumer currently deserializes values as `String`, and its trusted package (`com.smartsplit.events`) doesn't match the producer's `pt.saraborges.smartsplit.event`.

See that repository for its own setup details.

## Roadmap

Epics from the original backlog (`documentation/SmartSplit_Notion.md`, in Portuguese): Users, Groups, Expenses, Balances, Smart Settlement, Payments, History, Notifications, Dashboard.

Implemented so far: user registration, groups (create, add/remove members), equal-split expenses, currency conversion, `group.created` event. Balance, Category and Notification controllers are still empty stubs.

Planned: authentication (JWT), more split types, balances and settlement, Testcontainers, more Kafka events and consumers.
