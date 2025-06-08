# Academ-IQ API 1.1.0

Academ-IQ API is a backend application developed in Java using Spring Boot. It provides a RESTful API for managing courses, user enrollments, lesson progress, and features related to an educational platform.

## Main Features

- **User Management**: Registration, authentication, and user handling.
- **Courses**: Creation, retrieval, and management of courses.
- **Enrollments**: Users can enroll in courses, mark them as favorites, archive them, and mark them as completed.
- **Progress Tracking**: Tracks user progress through each course’s lessons.
- **Access Control**: Token-based security and custom filter configuration.
- **Custom Exceptions**: Robust error handling with clear responses.

## Project Structure

- `src/main/java/dev/luiiscarlos/academ_iq_api/`
  - `controllers/`: REST controllers for various resources.
  - `models/`: Entities and DTOs.
  - `repositories/`: Interfaces for data access.
  - `services/`: Business logic (e.g., [`EnrollmentService`](src/main/java/dev/luiiscarlos/academ_iq_api/services/EnrollmentService.java)).
  - `configurations/`: Security configuration and utilities.
  - `exceptions/`: Custom exceptions.
  - `utilities/`: Various utilities.
- `src/main/resources/`: Configuration files and initial data.
- `test/`: Unit and integration tests.

## Requirements

- Java 21+
- Maven
- Docker

## Running Locally

1. Clone the repository.
2. Set up the required environment variables in `.env` or `application.properties`.
3. Install dependencies and build:
```sh
    ./mvnw clean install
```
4. Run the aplication
```sh
    ./mvnw spring-boot:run
```
5. The API will be available at `http://localhost:8080`

## NOTES
- Logs are stored in `data/logs/`
- You can modify settings in `src/main/resources/application.properties`

---

Developed by [LuiisCarlos](https://github.com/LuiisCarlos)
