# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview
This is a Spring Boot-based blog application with the following key features:
- Blog management system with posts, categories, tags, and comments
- User authentication and administration panel
- MySQL database with JPA/Hibernate for data persistence
- Redis caching for performance optimization
- Thymeleaf templates for server-side rendering
- Swagger API documentation
- Markdown support for blog content

## Technology Stack
- **Framework**: Spring Boot 2.5.12
- **Language**: Java 8
- **Database**: MySQL with JPA/Hibernate
- **Caching**: Redis
- **Frontend**: Thymeleaf templates, HTML/CSS/JavaScript
- **API Documentation**: Swagger 2.9.2
- **Build Tool**: Maven

## Project Structure
```
blog/
├── src/
│   ├── main/
│   │   ├── java/com/ws/
│   │   │   ├── BlogApplication.java (Main application class)
│   │   │   ├── aspect/ (Cross-cutting concerns)
│   │   │   ├── config/ (Configuration classes - Redis, Swagger)
│   │   │   ├── dao/ (Data access interfaces - JPA repositories)
│   │   │   ├── handler/ (Exception handlers)
│   │   │   ├── interceptor/ (Request interceptors)
│   │   │   ├── po/ (Persistent objects - JPA entities)
│   │   │   ├── service/ (Business logic layer)
│   │   │   ├── util/ (Utility classes)
│   │   │   ├── vo/ (Value objects)
│   │   │   └── web/ (Web controllers)
│   │   └── resources/
│   │       ├── application.yml (Main configuration)
│   │       ├── application-dev.yml (Development configuration)
│   │       ├── application-prod.yml (Production configuration)
│   │       ├── templates/ (Thymeleaf HTML templates)
│   │       ├── static/ (Static assets - CSS, JS, images)
│   │       └── i18n/ (Internationalization files)
│   └── test/
└── pom.xml (Maven configuration)
```

## Key Components
1. **Blog Entity**: Core blog post with title, content, tags, category, comments
2. **User Management**: Admin user authentication and authorization
3. **Admin Panel**: Full CRUD operations for blogs, categories, tags
4. **Frontend Pages**: Index, blog details, archives, tags, categories
5. **Comment System**: Nested comments with user information
6. **Search Functionality**: Full-text search across blog titles and content
7. **Caching Layer**: Redis caching for improved performance

## Common Development Tasks

### Running the Application
```bash
# Development mode
./mvnw spring-boot:run

# Or package and run
./mvnw clean package
java -jar target/blog-1.0.0.jar
```

### Database Configuration
The application uses MySQL with the following configuration:
- Database name: blog
- Default connection: localhost:3306
- Username: root
- Password: 169167866Spl.

### Redis Configuration
- Development: localhost:6379
- Production: 101.42.176.44:6379 with password authentication

### Testing
```bash
# Run all tests
./mvnw test

# Run tests for a specific class
./mvnw -Dtest=TestClassName test
```

## Configuration Profiles
- `dev`: Development environment with local MySQL and Redis
- `prod`: Production environment with remote Redis server

## API Documentation
Swagger UI is available at: http://localhost:81/swagger-ui.html

## Code Patterns
1. **JPA Entities**: Lombok annotations for boilerplate reduction
2. **Repository Pattern**: Spring Data JPA repositories with custom queries
3. **Service Layer**: Business logic separated from controllers
4. **Controller Layer**: REST endpoints and view controllers
5. **Thymeleaf Templates**: Server-side rendered HTML pages
6. **Exception Handling**: Global exception handlers for consistent error responses