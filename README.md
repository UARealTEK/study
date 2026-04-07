# Study: Book Borrowing Library API

## Purpose

A pet project to practice Java development and testing. Implements a library REST API with book borrowing system. Focus on AQA skills and automated testing excellence.

## Tech Stack

### Core Framework
- **Spring Boot 4.0.1** - Application framework with MVC, Data JPA, Validation
- **MySQL 9.3** - Production database
- **Java 21** - Programming language
- **Lombok** - Boilerplate reduction

### API & Mappers
- **SpringDoc/Swagger** - OpenAPI documentation
- **MapStruct** - DTO/Entity mapping with compile-time validation

### Testing (Core Focus)
- **JUnit 5** - Test framework with parameterization and tags
- **Mockito** - Dependency mocking for unit/integration tests
- **RestAssured** - API endpoint testing
- **Allure** - Test reporting and metrics

### Design Patterns
- **Strategy Pattern** - DTO generation strategies (valid/invalid objects)
- **Method Source** - Custom parameterized test data via annotations
- **Custom Annotations** - Test data generation with @RandomUserDto, @RandomPageResponseDto
- **DTOs** - Separated request/response models

## Service Layer

Each service encapsulates domain logic and repository access:

### UserService
Manages user profiles with search, filtering, and caching support.

### BookService
Manages book catalog with availability tracking (borrowed/available status).

### BorrowService  
Handles book lending/returning with transaction management and active borrow tracking.

## Database Schema

**users**
```
id (PK, auto) | full_name (NOT NULL) | age (NOT NULL) | gender | created_at
```

**books**
```
id (PK, auto) | name (NOT NULL) | author (NOT NULL) | isbn | created_at
```

**borrow_records**
```
id (PK, auto) | user_id (FK) | book_id (FK) | borrowed_at | returned_at | created_at
Constraint: UNIQUE(user_id, book_id) WHERE returned_at IS NULL
```

## Test Architecture

### Test Abstraction Layers

```
BaseTest (Test configuration & utilities)
├── BaseControllerTest (MockMvc setup for HTTP testing)
│   ├── BaseUserControllerTest
│   │   └── CRUDUserUserControllerTests
│   ├── BaseBookControllerTest
│   └── BaseB orrowControllerTest
│
└── BaseServiceTest (Service layer setup)
    ├── UserServiceTest
    ├── BookServiceTest
    └── BorrowServiceTest
```

### Test Slice Types

**Controller Tests** (`@WebMvcTest`)
- API contract testing via HTTP layer simulation
- Tests request validation, response structure, status codes
- Mocks service dependencies
- Uses MockMvc for endpoint invocation

Example:
```java
@WebMvcTest(UserController.class)
class UserControllerTests extends BaseUserControllerTest {
    // HTTP layer testing - validates API contracts
}
```

**Service Tests**  
- Business logic testing in isolation
- Tests service methods with repository mocking
- Validates business rules and transformations
- Pure unit testing without HTTP layer

Example:
```java
class UserServiceTest extends BaseServiceTest {
    // Service logic testing - validates calculations, state changes, workflows
}
```

## Test Data Generation

### ParameterResolver Pattern
Custom JUnit 5 ParameterResolvers provide automatic test data injection:

- `RandomUserDtoResolver` - Generates valid UserDto
- `RandomInvalidUserDtoResolver` - Generates UserDto with constraint violations  
- `RandomPageResponseDTOResolver` - Generates paginated responses

### PageGenerationStrategy Interface
Central strategy interface for both valid and invalid DTO generation:

```java
public interface PageGenerationStrategy {
    <T> List<T> generate(Class<T> clazz, int count);  // Valid objects
    Object generateInvalidObj(...);  // Single invalid object
    List<?> generateInvalidObjList(Class<?> clazz, int count);  // Invalid list
}
```

**Implementations:**
- `RandomStrategy` - Random valid objects
- `SameObjStrategy` - Repeating same object
- `EmptyStrategy` - Empty list

### GenericDtoInvalidStrategy
Generates DTOs with specific constraint violations for negative testing. Supports:
- `@NotBlank` - Violates non-empty requirement
- `@Size` - Violates size boundaries
- `@Min/@Max` - Violates numeric ranges

### Usage Examples

```java
@Test
void testSave(@RandomUserDto UserDto user) {
    // user is auto-injected and valid
}

@Test
void testValidation(@RandomInvalidUserDto UserDto invalid) {
    // invalid user violates one constraint
}

@ParameterizedTest  
@RandomPageResponseDto(strategy = PageStrategyType.RANDOM, size = 10)
void testPagination(PageResponseDTO<UserDto> page) {
    // page with 10 random users
}
```

## Test Tags & Custom Runs

Control test execution with custom tags:

```java
@Smoke  // Critical smoke tests
@Feature("User Management")  // Feature grouping
@Story("User CRUD")  // Story mapping
@Epic("User Management")  // Epic categorization
```

Run specific tests:
```bash
./gradlew test --tests @Smoke
./gradlew test --tests *User*
./gradlew test --tests CRUDUserUserControllerTests
```

## Testing Tools & Techniques

**ArgumentCaptor** - Capture and verify method arguments:
```java
ArgumentCaptor<UserDto> captor = ArgumentCaptor.forClass(UserDto.class);
verify(service).saveUser(captor.capture());
assertEquals(expected, captor.getValue());
```

**assertAll()** - Group multiple assertions:
```java
assertAll(
    () -> assertEquals(200, status),
    () -> assertNotNull(body),
    () -> assertTrue(body.getId() > 0)
);
```

**Recursive Comparison** - Deep object comparison:
```java
assertThat(actual)
    .usingRecursiveComparison()
    .isEqualTo(expected);
```

## DTO Structure

### Request/Response DTOs
- `UserDto` - User profile model
- `BookDto` - Book catalog model
- `BorrowRecordRequestDto` - Borrow/return request
- `BorrowRecordResponseDto` - Borrow record with mapped relationships

### Pagination Response
All list endpoints return `PageResponseDTO<T>`:
```json
{
    "content": [...],
    "pageNumber": 0,
    "pageSize": 5,
    "totalElements": 42,
    "totalPages": 9
}
```

## Allure Test Reporting

Tests include Allure annotations for detailed metrics:

```java
@Epic("User Management")
@Feature("CRUD Operations")
@Story("Create Users")
@DisplayName("Should save valid user")
void testSaveValidUser(@RandomUserDto UserDto user) { }
```

Generate reports:
```bash
./gradlew test
./gradlew allureReport
open build/reports/allure/index.html
```

## Local Development Setup

### Prerequisites
- Java 21+
- MySQL 9.3+
- Gradle 9.x

### Database Setup

```sql
CREATE DATABASE study_db;
USE study_db;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_book (name, author)
);

CREATE TABLE borrow_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    borrowed_at TIMESTAMP NOT NULL,
    returned_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY active_borrow (user_id, book_id, returned_at),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);
```

Update `application.properties` with MySQL connection details.

### Run Application

```bash
./gradlew bootRun
# http://localhost:8080
# http://localhost:8080/swagger-ui.html
```

### Run Tests

```bash
./gradlew test                      # All tests
./gradlew test --tests "*Controller*"  # Only controller tests
./gradlew test --tests "*Service*"     # Only service tests
./gradlew test --tests CRUDUserUserControllerTests
```

## API Endpoints

### Users
- `GET /users` - List with pagination
- `GET /users/{id}` - Get user
- `GET /users/{id}/borrows` - User's borrow history
- `POST /users` - Create user
- `PUT/PATCH /users/{id}` - Update user
- `DELETE /users/{id}` - Delete user

### Books
- `GET /books` - List with filtering
- `GET /books/available` - Available books only
- `GET /books/borrowed` - Borrowed books only
- `GET /books/{id}` - Get book
- `POST /books` - Add book
- `PUT/PATCH /books/{id}` - Update book
- `DELETE /books/{id}` - Delete book

### Borrows
- `GET /borrows` - List all borrows
- `POST /borrows` - Borrow book
- `PATCH /borrows` - Return book
- `GET /borrows/{id}` - Get record

## Coverage

**Base CRUD:** Create, Read, Update, Delete, List for all entities

**Domain Features:**
- Book availability status
- Active borrow tracking
- User borrow history
- Input validation
- Error handling

## Validation Rules

- **User**: fullName (NotBlank), age (18-120), gender (MALE/FEMALE/OTHER)
- **Book**: name (NotBlank), author (NotBlank), isbn (optional)
- **Borrow**: bookId + userId must exist, unique active borrow per pair
- **Integration Testing**: Full application context tests where needed.
- **Parameterized Tests**: Leveraging JUnit 5's Method Source and custom Parameter Resolvers for data-driven testing.
- **Custom Annotations and Tags**: For categorizing and running specific test suites.
- **Reporting**: Allure for visual, detailed test reports with annotations like @DisplayName, @Feature, @Story, etc., to make results more appealing and informative.
- **Testing Instruments**: Utilizes ArgumentCaptors for verifying method arguments, assertAll() for grouped assertions to improve readability, and recursive comparison for deep object equality checks.
- **Test Data Generation**: Custom strategies and resolvers for generating random or specific test data.

## Approaches and Patterns Used
- **Strategy Pattern**: Implemented for flexible handling of different operations, validations, and test data generation (e.g., in StrategyEngine for DTO and Page strategies).
- **Method Source Approach**: Utilized in JUnit 5 for parameterized tests, allowing data-driven testing with multiple inputs.
- **Custom Annotations**: Created for aspect-oriented programming, test categorization (@Unit, @Integration), and metadata handling.
- **Allure Reporting**: Enhances test visibility with rich reports, impacting test maintainability and stakeholder communication. Annotations like @Feature, @Story are used to provide descriptive test names and grouping in reports.
- **MapStruct**: Automates DTO-entity mapping, reducing manual code and errors, impacting data layer efficiency.

## Test Slice Structure
- **Controller Tests**: API slice tests focusing on endpoint behavior, request/response handling, and integration with services.
- **Service Tests**: Logic testing for business rules, mocking external dependencies to isolate unit behavior.

## Abstraction Structure
The test architecture follows a layered abstraction for reusability and maintainability:
- **BaseTest**: The foundation class providing common utilities like mappers (e.g., UserMapper), ObjectMapper for JSON handling, and helper methods (e.g., anySpec() for Specification mocking).
- **BaseServiceTest**: Extends BaseTest, sets up mocks for repositories (e.g., @Mock UserRepository) and injects them into services (@InjectMocks UserService). Handles initialization and cleanup.
- **BaseControllerTest**: Extends BaseTest, configures MockMvc for controller testing, mocking services and repositories as needed.
- **Concrete Test Classes**: Extend the appropriate base (e.g., CRUDUserServiceTests extends BaseServiceTest), focusing on specific test logic.
- **Abstraction Levels**: BaseTest provides low-level utilities; BaseServiceTest/BaseControllerTest add layer-specific setup; concrete classes implement test scenarios.
- **Parameter Resolvers**: Custom implementations of JUnit 5's ParameterResolver interface for injecting test data (e.g., random DTOs, pages). They use strategies to generate data based on annotations. [JUnit 5 ParameterResolver](https://junit.org/junit5/docs/current/user-guide/#extensions-parameter-resolution).

## DTO Structure
### In the Project
- **UserDto**: Represents user data for API responses (e.g., fullName, age, gender).
- **BookDto**: Represents book data (e.g., name, author).
- **BorrowRecordRequestDto/BorrowRecordResponseDto**: For borrow operations (request: bookId, userId; response: includes timestamps).
- **PageResponseDTO**: Generic paginated response wrapper (content list, page metadata).
- **Entities**: Mirror DTOs but include JPA annotations (e.g., UserEntity, BookEntity).

### In Tests
- **Test DTOs**: Similar to project DTOs but often extended with invalid variants for validation testing.
- **Random Resolvers**: Generate random instances of DTOs/Entities for parameterized tests.
- **Strategies**: For creating specific test data (e.g., invalid DTOs with missing fields).

## Modules and Components
### Controllers
- **BorrowController**: Handles borrowing and returning of books.
- **LibraryController**: Manages book-related operations (likely CRUD for books).
- **UserController**: Manages user-related operations (CRUD for users).

### Services
- **BookService**: Provides business logic for book management.
- **BorrowService**: Handles borrowing records and related operations.
- **UserService**: Manages user data and authentication/logic.

### Repositories
- **BookRepository**: Data access for books.
- **BorrowRecordsRepository**: Data access for borrow records.
- **UserRepository**: Data access for users.

### DTOs and Entities
- **DTOs**: Data Transfer Objects for API requests/responses.
- **Entities**: JPA entities representing database tables.

### Utilities
- **Converters**: MapStruct mappers for object conversion.
- **Exceptions**: Custom exception handling.
- **Enums**: For constants and status values.

## Coverage
The project covers basic CRUD operations for users, books, and borrowing records. It includes a domain model for a book borrowing API, with features like pagination, validation, and transactional operations.

## How to Run
1. Ensure Java 21 is installed.
2. Clone the repository.
3. Run `./gradlew build` to build the project.
4. Run `./gradlew bootRun` to start the application.
5. Access the API at `http://localhost:8080`.
6. For tests: `./gradlew test`.
7. For Allure reports: `./gradlew allureServe` after tests.

## Links
- [Spring Boot](https://spring.io/projects/spring-boot)
- [MapStruct](https://mapstruct.org/)
- [Allure](https://docs.qameta.io/allure/)
- [JUnit 5](https://junit.org/junit5/)
- [RestAssured](https://rest-assured.io/)
- [JUnit 5 ParameterResolver](https://junit.org/junit5/docs/current/user-guide/#extensions-parameter-resolution)
