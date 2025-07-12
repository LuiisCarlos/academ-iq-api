> Academ-IQ API

Academ-IQ API is a backend application built with Java and Spring Boot. It provides a robust RESTful API for managing courses, user enrollments, lesson progress, ratings, and other features for an educational platform.

---

## Table of Contents

- [Table of Contents](#table-of-contents)
- [Features](#features)
- [Project Structure](#project-structure)
- [Requirements](#requirements)
- [Configuration](#configuration)
- [Running Locally](#running-locally)
- [API Overview](#api-overview)
- [Logging](#logging)
- [Author](#author)

---

## Features

- **User Management**: Registration, authentication, profile updates, and password recovery.
- **Course Management**: Create, update, retrieve, and delete courses with sections and lessons.
- **Enrollments**: Users can enroll in courses, mark as favorite, archive, and track completion.
- **Progress Tracking**: Track user progress through lessons and sections.
- **Reviews**: Users can rate and comment on courses.
- **File Uploads**: Upload and manage files (images, videos) for courses and user avatars.
- **Security**: JWT-based authentication and role-based access control.
- **Admin Tools**: Admin endpoints for user and course management.
- **Custom Exceptions**: Clear and robust error handling.

---

## Project Structure

```
academ-iq-api/
│
├── src/
│   ├── main/
│   │   ├── java/dev/luiiscarlos/academ_iq_api/
│   │   │
│   │   ├── config/                         # Global system configurations (security, web, Stripe, etc.)
│   │   │
│   │   ├── domain/                         # Cross-cutting and secondary domains
│   │   │   ├── billing/                    # Everything related to payments and subscriptions
│   │   │   │   ├── payment/                # Integration with payment gateways
│   │   │   │   ├── subscription/           # Plan and subscription management
│   │   │   │   └── webhook/                # Receiving events from external services (like Stripe)
│   │   │   └── notification/
│   │   │       └── mail/                   # Email notification service (e.g. verification)
│   │   │
│   │   ├── features/                       # Main modules organized by business domain
│   │   │   ├── identity/                   # Everything related to user identity
│   │   │   │   ├── auth/                   # Authentication logic (login, register, etc.)
│   │   │   │   └── user/                   # User management
│   │   │   │       ├── controller/         # REST controllers
│   │   │   │       ├── dto/                # Data Transfer Objects
│   │   │   │       ├── exception/          # Module-specific exceptions
│   │   │   │       ├── facade/             # Facades to abstract multiple layers
│   │   │   │       ├── mapper/             # Entity-to-DTO converters
│   │   │   │       ├── model/              # Database entities
│   │   │   │       ├── repository/         # Data access repositories (JPA)
│   │   │   │       ├── service/            # Business logic
│   │   │   │       │   └── impl/           # Concrete service implementations
│   │   │   │       └── structure/role/     # Role and permission management for users
│   │   │   │
│   │   │   ├── learning/                   # The core of the app: learning domain
│   │   │   │   ├── category/               # Course categories
│   │   │   │   │   └── structure/benefit/  # Sub-feature: category benefits
│   │   │   │   ├── course/                 # Course management (create, edit, fetch, etc.)
│   │   │   │   │   └── structure/          # Sub-features specific to course
│   │   │   │   │       ├── section/        # Course sections
│   │   │   │   │       └── lesson/         # Course lessons
│   │   │   │   ├── enrollment/             # Course enrollments
│   │   │   │   └── review/                 # Course reviews and ratings
│   │   │   │
│   │   │   └── storage/                    # File storage, uploading resources, etc.
│   │   │
│   │   ├── shared/                         # Reusable code across modules
│   │   │   ├── constants/                  # Global constants
│   │   │   ├── exception/                  # Global error handling and standard responses
│   │   │   ├── filter/                     # Security filters and exception handling
│   │   │   ├── interceptor/                # Interceptors like timing/logging
│   │   │   ├── security/                   # Custom security (handlers, contexts, etc.)
│   │   │   ├── util/                       # General utilities (e.g. HTML template processor)
│   │   │   └── validation/                 # Custom validations (e.g. strong password checks)
│   │   │
│   │   └── ApiAcademIqApplication.java     # Main class to run the Spring Boot application
│   │
│   ├── resources/
│   │   ├── keys/                           # Public/private keys if used (e.g. JWT, Stripe)
│   │   ├── templates/                      # HTML templates for emails
│   │   │   └── verificationMail.html       # Email verification template
│   │   └── application.properties          # Spring Boot config
│
├── test/                                   # Unit and integration tests
│   └── java/dev/luiiscarlos/api_academ_iq/
│
├── .editorconfig                           # Shared code format and style rules
├── .env.dev                                # Environment variables for dev
├── .env.prod                               # Environment variables for prod
├── .gitignore                              # Files/directories ignored by Git
├── Dockerfile                              # Docker image for deployment
├── pom.xml                                 # Maven dependencies and config
└── README.md                               # This file
```

---

## Requirements

- Java 21+
- Maven
- Docker (optional, for containerized DB or deployment)

---

## Configuration

1. **Environment Variables**
   Copy `.env.example` to `.env` and fill in your values:
   ```sh
   cp .env.example .env
   ```
   Or set variables in `src/main/resources/application.properties`.

2. **Database**
   The project uses PostgreSQL by default. Update your DB credentials in `.env` or `application.properties`.

3. **Mail, Stripe, Cloudinary**
   Configure SMTP, Stripe, and Cloudinary credentials as needed for your features.

---

## Running Locally
1. **Clone the repository**
   ```sh
   git clone https://github.com/LuiisCarlos/academ-iq-api.git
   cd academ-iq-api
   ```

2. **Configure environment**
   - Edit `.env` or `src/main/resources/application.properties` as described above.

3. **Build the project**
   ```sh
   mvn clean package -DskipTests
   ```

4. **Run the application in development mode**
   ```sh
   mvn spring-boot:run -Dspring-boot.run.profiles=dev -Dspring.devtools.add-properties=true
   ```
   The API will be available at [http://localhost:8080](http://localhost:8080).

5. **Run the application in production mode**
   ```sh
   java -Dspring.profiles.active=prod -Dspring.devtools.add-properties=false -jar target/<enter_the_current_version>.jar
   ```

6. **Run tests**
   ```sh
   mvn test
   ```
---

## API Overview

- **Base URL:** `/api/v1/`
- **Authentication:** JWT Bearer tokens via `Authorization` header
- **Main Endpoints:**
  - `/auth` - User registration, login, password reset
  - `/users` - User profile and admin management
  - `/courses` - Course CRUD and listing
  - `/enrollments` - User enrollments and progress
  - `/reviews` - Course ratings and comments
  - `/files` - File upload and retrieval
  - `/admin` - Admin management

See the [controllers](src/main/java/dev/luiiscarlos/academ_iq_api/controllers/) for detailed endpoint definitions.

---

## Logging

- Logs are stored in `data/logs/` by default.
- Logging configuration can be changed in `src/main/resources/application.properties`.

---

## Author

Developed by [Luis Carlos Caicedo Giraldo](https://github.com/LuiisCarlos)

---
