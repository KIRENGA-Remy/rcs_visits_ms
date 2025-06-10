# RCS Visiting Management System

A comprehensive digital solution for managing prison visiting activities, built with Spring Boot.

## Project Overview

This system digitizes the visiting activities management process for prisons, providing a secure and efficient way to manage visitor requests, approvals, and visitations.

## Tech Stack

### Backend
- Spring Boot
- Spring Security with JWT
- Spring Data JPA
- PostgreSQL
- Swagger UI for API Documentation

## Features

### Backend Features
- Role-based authentication and authorization (ROLE_ADMIN, ROLE_VISITOR, ROLE_LAWYER)
- Prisoner management system
- Visitor registration and management
- Lawyer registration and management
- Visit request approval system
- Digital slip generation (LAWYER_SLIP, VISITOR_SLIP)
- Visit scheduling system
- Comprehensive API documentation

## Project Structure

```
rcs_visiting_ms/
├── backend/              # Spring Boot application
├── frontend/            # React Native Expo app
├── docs/                # Project documentation
└── scripts/             # Setup and utility scripts
```

## Data Flow Diagram

```
[Visitor/Lawyer]
     ↓
[Mobile App]
     ↓
[API Gateway]
     ↓
[Spring Boot Backend]
     ↓
[PostgreSQL Database]
```

## Getting Started

### Prerequisites
- Java 17+
- Node.js 18+
- PostgreSQL 14+
- Expo CLI

### Backend Setup
1. Navigate to `backend` directory
2. Run `mvn clean install`
3. Configure application.properties
4. Run `mvn spring-boot:run`

## Security

- JWT-based authentication
- Role-based access control
- Secure password hashing
- Input validation
- SQL injection prevention

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

MIT License
