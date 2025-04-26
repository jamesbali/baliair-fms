# BaliAir Flight Management System (FMS)

## Overview
BaliAir FMS is a monolithic Spring Boot application developed to manage airline operations, including aircraft management, crew assignments, flight scheduling, passenger booking, and notifications.

The application runs both **locally via Docker Compose** and is **deployed to AWS App Runner** through a **CI/CD pipeline using GitHub Actions**, **DockerHub**, and **Amazon Elastic Container Registry (ECR)**.

---

## Features
- Aircraft Management
- Crew Member Management
- Flight Scheduling and Flight Delay Reporting
- Passenger Registration and Management
- Flight Booking and Booking Cancellation
- Secure Authentication and Authorization (JWT Tokens with Role-Based Access Control)
- Full Local and Cloud Environment Setup
- CI/CD Automated Pipeline
- Unit Testing for Services and Controllers
- API Testing with Postman Collections
- UML Class Diagram and ER Diagram included

---

## Technologies Used
- Java 21
- Spring Boot 3
- Gradle with Kotlin DSL
- Spring Security with JWT
- PostgreSQL (Docker for Local, AWS RDS for Cloud)
- Docker & Docker Compose
- AWS App Runner
- AWS Elastic Container Registry (ECR)
- GitHub Actions (for CI/CD)
- Postman (for API Testing)

---

## Local Development Setup

1. Clone the Repository:
   ```bash
   git clone https://github.com/jamesbali/baliair-fms.git
   cd baliair-fms
   ```

2. Run Locally using Docker Compose:
   ```bash
   docker compose up --build
   ```

3. Local Application URL:
   ```
   http://localhost:8080
   ```

4. Local PostgreSQL Database Connection:
   ```
   Host: localhost
   Port: 5436
   Username: bali_user
   Password: bali_pass
   Database: bali_air_db
   ```

---

## Cloud Deployment Details

- Environment: AWS App Runner
- Database: AWS RDS PostgreSQL Instance
- Cloud Application URL:
   ```
   https://ssknjjgm5c.us-east-1.awsapprunner.com
   ```
- Deployment Pipeline: GitHub Actions → Docker Build → Push to Amazon ECR → Deploy to AWS App Runner
---

## Testing Strategy

- Unit tests for all repository, service, and controller layers were written using JUnit and Mockito.
- Postman was used for testing all RESTful endpoints:
    - Admin registration and login
    - Aircraft management
    - Crew member management
    - Flight creation, updating, searching
    - Passenger registration and self-registration
    - Flight booking and booking cancellation
- Postman collections were used for structured testing of both local and cloud deployments.

---

## Diagrams

- UML Class Diagram (`/docs/UML_Class_Diagram.png`)
- ER Diagram (`/docs/ER_Diagram.png`)

The diagrams illustrate the overall architecture, entity relationships, and database schema used in the BaliAir FMS system.

---

## Project Structure

```
com.bali.baliairfms
 ├── model             # JPA Entities (Aircraft, CrewMember, Passenger, etc.)
 ├── controller        # REST Controllers
 ├── service           # Business Logic Services
 ├── repository        # Spring Data JPA Repositories
 ├── security          # JWT Authentication and Security Configurations
 ├── dto               # Data Transfer Objects (Request/Response)
 ├── mapper            # Application model Configurations
 └── exception         # Global Exception Handling
 |-- specification     # Creteria search
```
Project Structure
com.bali.baliairfms
├── model             # JPA Entities (Aircraft, CrewMember, Passenger, etc.)
├── controller        # REST Controllers
├── service           # Business Logic Services
├── repository        # Spring Data JPA Repositories
├── security          # JWT Authentication and Security Configurations
├── dto               # Data Transfer Objects (Request/Response)
├── mapper            # Application model Configurations
└── exception         # Global Exception Handling




