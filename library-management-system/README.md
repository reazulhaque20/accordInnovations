# Library Management System

A comprehensive Spring Boot 3 application for managing library operations with all required features from the exercise.

## Features Implemented

✅ **All HTTP Methods** - GET, POST, PUT, DELETE for book management  
✅ **Search with Pagination** - Advanced search with multiple filters  
✅ **AspectJ Logging** - Comprehensive request/response logging  
✅ **In-memory Database** - H2 database with console access  
✅ **JOIN Query** - Example query joining books and authors  
✅ **External API Integration** - Google Books API and generic API calls  
✅ **Unit Tests** - Comprehensive test coverage  
✅ **Integration Tests** - Repository and service layer tests  
✅ **Postman Collection** - Complete API documentation

## Prerequisites

- Java 17 or higher
- Maven 3.8+
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

## Setup and Run

1. **Clone and build the project:**
```bash
git clone <repository-url>
cd library-management-system
mvn clean install
```

Run the application:

```bash
mvn spring-boot:run
```

Access the application:
~~~
Application: http://localhost:8181/library
~~~
~~~
H2 Console: http://localhost:8181/library/h2-console

JDBC URL: jdbc:h2:mem:librarydb

Username: sa

Password: (leave empty)
~~~
~~~
Swagger UI: http://localhost:8181/library/swagger-ui.html
~~~
# API Endpoints

## Book Management

* GET /api/v1/books - Get all books with pagination

* GET /api/v1/books/{id} - Get book by ID

* POST /api/v1/books - Create new book

* PUT /api/v1/books/{id} - Update existing book

* DELETE /api/v1/books/{id} - Delete book

* POST /api/v1/books/search - Search books with filters

## External APIs

* GET /api/v1/books/external/search - Search Google Books API

* GET /api/v1/books/external/call - Call any external API

## Database Queries

* GET /api/v1/books/loans/active - JOIN query example

## Sample Data

The application automatically loads sample data on startup:

4 Authors (George Orwell, J.K. Rowling, Harper Lee, F. Scott Fitzgerald)
6 Books with various genres and quantities

## Testing

Run all tests:

```bash
mvn test
```

Run specific test class:

```bash
mvn test -Dtest=BookControllerTest
```

## Project Structure
```
src/main/java/com/library/
├── aspect/           # AspectJ logging
├── config/          # Configuration classes
├── controller/      # REST controllers
├── dto/            # Data Transfer Objects
├── loader/         # Sample data loader
├── model/          # JPA entities
├── repository/     # Data access layer
└── service/        # Business logic layer
```

## Technology Stack

1. **Spring Boot 3 - Application framework**<br/>
2. **Java 17 - Programming language**<br/>
3. **H2 Database - In-memory database**<br/>
4. **Spring Data JPA - Data persistence**<br/>
5. **AspectJ - Request/response logging**<br/>
6. **RestTemplate - External API calls**<br/>
7. **JUnit 5 - Testing framework**<br/>
8. **Mockito - Mocking framework**<br/>
9. **Lombok - Code generation**<br/>
10. **OpenAPI/Swagger - API documentation**<br/>

## Logging

The application uses AspectJ to log all HTTP requests and responses. Logs include:

1. **Request method and URL**<br/>
2. **Request parameters**<br/>
3. **Execution time**<br/>
4. **Response data**<br/>
5. **Any exceptions**<br/>

## Postman Collection
Import the provided LMS.json into Postman to test all endpoints.

## Future Enhancements

1. Add authentication and authorization
2. Implement book borrowing and return functionality
3. Add email notifications for due dates
4. Implement advanced search with full-text search
5. Add caching for performance
6. Dockerize the application
7. Add API rate limiting