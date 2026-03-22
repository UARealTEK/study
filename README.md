# Study Project

## Purpose
Pet project to learn Java and testing. Builds a library API with CRUD operations and book borrowing logic. Focuses on AQA skills in automated testing.

## Tech Stack
- **Java 21**: The programming language used for development.
- **Spring Boot 4.0.1**: Framework for building the REST API, providing dependency injection, web MVC, and data JPA.
- **Spring Data JPA**: For ORM and database interactions.
- **MySQL**: Primary database for production (with MySQL Connector/J).
- **H2 Database**: In-memory database for testing.
- **Lombok**: Reduces boilerplate code with annotations.
- **MapStruct**: For object mapping between DTOs and entities.
- **Hibernate Validator**: For bean validation.
- **SpringDoc OpenAPI**: Generates API documentation.
- **JUnit 5**: Testing framework, including support for tags to enable custom test runs (e.g., grouping tests by type or feature).
- **Mockito**: For mocking dependencies in unit tests.
- **RestAssured**: For API integration testing.
- **Allure**: For generating detailed test reports.
- **Spring RestDocs**: For API documentation generation.

## Database Setup
This project requires a local MySQL database to run. Set up MySQL and create the following schema:

### Tables
- **users**
  - `id` BIGINT PRIMARY KEY AUTO_INCREMENT
  - `full_name` VARCHAR(255) NOT NULL
  - `age` INT NOT NULL
  - `gender` VARCHAR(50) NOT NULL

- **books**
  - `id` BIGINT PRIMARY KEY AUTO_INCREMENT
  - `name` VARCHAR(255) NOT NULL
  - `author` VARCHAR(255) NOT NULL

- **borrow_records**
  - `id` BIGINT PRIMARY KEY AUTO_INCREMENT
  - `user_id` BIGINT NOT NULL, FOREIGN KEY REFERENCES users(id)
  - `book_id` BIGINT NOT NULL, FOREIGN KEY REFERENCES books(id)
  - `borrowed_at` DATETIME NOT NULL
  - `returned_at` DATETIME NULL

Update `application.properties` or `application.yml` with your MySQL connection details (e.g., URL, username, password).

## Testing Stack
The testing stack is a core component of this project, reflecting the focus on AQA practices:
- **Unit Testing**: Service layer tests (logic testing) using Mockito for mocking and JUnit 5 for assertions.
- **API Slice Testing**: Controller tests simulating API endpoints with MockMvc.
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
