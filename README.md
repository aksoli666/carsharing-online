# Carsharing Online - Spring Boot Project

A modern digital solution that transforms traditional car-sharing operations into a streamlined, user-friendly service. This system eliminates manual processes, enables online bookings, and provides comprehensive management tools.

## Key Features
- Real-time car availability tracking and online booking
- Digital payment processing via Stripe integration
- Automated notifications through Telegram
- Comprehensive rental and payment management

## Technical Stack
- Backend: Spring Boot, Spring Security, Spring Data JPA
- Database: SQL with Liquibase migration
- Integration: Stripe API, Telegram Bot API
- Deployment: Docker containerization

## Architecture

![architecture](https://snipboard.io/v4O3HU.jpg)

## Core Components

The system consists of five main domain models: User, Role, Car, Rental, and Payment, working together to provide a seamless car-sharing experience.

## UML ER Diagram

```
+----------------+      +-----------------+      +------------------+
|      Car       |      |    Rental       |      |     Payment      |
+----------------+      +-----------------+      +------------------+
| id             |<---+ | id              |      | id               |
| model          |      | rentalDate      |      | status           |
| brand          |      | returnDate      |      | type             |
| type           |      | actualReturnDate|      | sessionUrl       |
| inventory      |      | car_id          | ---> | sessionId        |
| dailyFee       |      | user_id         | ---> | amount           |
| isDeleted      |      | payment_id      | <--- | rental_id        |
+----------------+      +-----------------+      +------------------+    
            ^                      ^                      |
            |                      |                      |
            +----------------------+----------------------+
            |
            |
       +----------------+
       |      User      |
       +----------------+
       | id             |
       | email          |
       | firstName      |
       | lastName       |
       | password       |
       | telegramChatId |
       | isDeleted      |
       +----------------+
            |
            v
       +---------------+
       |     Role      |
       +---------------+
       | id            |
       | role          |
       +---------------+
```

## API Features
- Authentication and user management
- Car inventory and rental operations
- Payment processing and tracking
- Automated notification system

## Getting Started
- [Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Java JDK 17+](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Git](https://git-scm.com/)
- [Postman](https://www.postman.com/downloads/) (optional)

Quick Start:

``` plaintext
git clone https://github.com/your-username/your-repo-name.git
cd your-repo-name
mvn clean package
docker-compose up --build
```
## Api Testing
Import the provided [Postman collection](https://www.postman.com/supply-astronomer-36769183/car-sharing-service-management-system/collection/00jmifh/car-sharing-service-management-system?action=share&source=copy-link&creator=34116334) to test API endpoints. Base URL: localhost:8088

## [View Demo](https://olexsandradorofeieiva.wistia.com/medias/vxox3ok1fa) 
