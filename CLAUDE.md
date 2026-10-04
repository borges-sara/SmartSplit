# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

SmartSplit is a Splitwise-alternative REST API, built as a Java learning project. Stack: Java 21, Spring Boot, Maven, PostgreSQL, Spring Security, Flyway, MapStruct, Lombok. Kafka and Testcontainers are planned but not yet integrated.
This project is intended to help the developer deepen their expertise in Java, Spring Boot, Docker, and Kafka. When responding to any question or request, provide clear, well-explained answers that not only solve the problem but also explain the underlying concepts, reasoning, and best practices. Prioritize educational value over brevity—the primary goal of this project is learning.
## Commands

```bash
# Start Postgres (required before running the app — see src/main/resources/application.properties)
docker-compose up -d

# Build / compile
./mvnw compile

# Run the full test suite
./mvnw test

# Run a single test class
./mvnw test -Dtest=SmartSplitApplicationTests

# Run a single test method
./mvnw test -Dtest=SmartSplitApplicationTests#contextLoads

# Run the app
./mvnw spring-boot:run
```

Swagger UI is available at `/swagger-ui.html` once the app is running (see `config/OpenApiConfig`).

## Architecture

The codebase follows a **layered (package-by-layer) structure** under `pt.saraborges.smartsplit`, not package-by-feature:

- `controller/` — REST controllers, one per domain (`UserController`, `BalanceController`, etc.)
- `service/` — business logic, `@Service` + constructor injection via Lombok `@AllArgsConstructor`
- `repository/` — Spring Data JPA interfaces extending `JpaRepository`
- `entity/` — JPA entities, each domain in its own subpackage (`entity/user/`, `entity/balance/`, `entity/category/`, `entity/expense/`, `entity/group/`, `entity/notification/`); value objects live under `entity/<domain>/valueobject/` (e.g. `entity/user/valueobject/Email.java`)
- `dto/request/`, `dto/response/` — request/response DTOs, implemented as Java records
- `mapper/` — MapStruct mappers, one per domain
- `validator/` — standalone validator classes (not JSR-380/Bean Validation)
- `config/` — Spring `@Configuration` classes
- `exception/` — custom exception hierarchy, with `exception/handler/` for the global `@RestControllerAdvice`

Only `User` is implemented end-to-end so far (register-user flow); the other domain entities/controllers (`Balance`, `Category`, `Expense`, `Group`, `Notification`) are currently empty stubs establishing the package layout for future work.

### Entity base class

`entity/BaseEntity.java` is a shared (non-`@MappedSuperclass`) base providing `id`, `createdAt`, `createdBy`, `updatedAt`, `updatedBy`. It intentionally exposes several constructors of different arity for subclasses to call via `super(...)` depending on which audit fields they set at creation time — when adding a new entity, pick the matching `super(...)` overload rather than adding new ones unless a genuinely new combination of fields is needed.

### Exception handling

- `exception/BaseException` is a `RuntimeException` carrying an HTTP status `code`.
- Concrete subclasses (`ValidationException` 400, `UnauthorizedException` 401, `ForbiddenException` 403, `ResourceNotFoundException` 404, `ConflictException` 409, `BusinessRuleException` 422, `InternalServerErrorException` 500, `ServiceUnavailableException` 503) each hardcode their status in the constructor.
- `exception/handler/GlobalExceptionHandler` (`@RestControllerAdvice`) catches `BaseException` and converts it to an `ErrorResponse` with the matching status code. New domain-level failures should throw one of these rather than a raw exception.

### Validators and value objects

- `validator/BasicValidator` is a base class (not a Spring bean itself) exposing `fail(message)`, which throws a `ValidationException` prefixed with the validator's name.
- `EmailValidator` and `PasswordValidator` extend it and are wired as Spring beans **manually** in `config/ValidatorConfig` (`@Bean` methods) rather than via `@Component` — do not add `@Component` to validator classes, since that would create duplicate bean definitions alongside `ValidatorConfig`.
- Value objects (`Email`, `Password` under `entity/user/valueobject/`) are constructed through static factories (`Email.newEmail(...)`, `Password.fromPlainText(...)`) that take the relevant validator as a parameter and validate before construction, keeping invalid instances unrepresentable.

### Mapping

Mappers (e.g. `UserMapper`) are abstract classes annotated `@Mapper(componentModel = SPRING)` with hand-written method bodies (not MapStruct-generated mapping logic) and `@Autowired` fields for validators used during construction (e.g. building `Email`/`Password` value objects). The `mapstruct-processor` **must** stay registered in `pom.xml`'s `maven-compiler-plugin` `annotationProcessorPaths` (both `default-compile` and `default-testCompile` executions) — without it, MapStruct never generates the `@Component`-annotated impl class and Spring has no bean to inject for the mapper, breaking startup.

### Security

`config/SecurityConfig` permits Swagger paths and `POST /users` (registration) without authentication; everything else requires authentication. There is no authentication mechanism (JWT or otherwise) wired up yet, despite it being on the roadmap.

## Product/roadmap context

See `documentation/SmartSplit_Notion.md` for the original feature backlog (in Portuguese) — epics: Users, Groups, Expenses, Balances, Smart Settlement, Payments, History, Notifications, Dashboard. Note it describes an earlier package-by-feature architecture and an older base package (`pt.teunome.smartsplit`); the actual code has since moved to the package-by-layer structure described above under `pt.saraborges.smartsplit`.
