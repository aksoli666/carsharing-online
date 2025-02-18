# Car Sharing Service Management System

## Project Overview

The Car Sharing Service Management System modernizes the traditional manual car-sharing process. 
It replaces outdated paper-based systems with a comprehensive, feature-rich, and user-friendly digital solution. 
This system aims to address issues such as untracked rentals, cash-only payments, and poor visibility into car 
availability while significantly improving the experience for customers and administrators alike.

## Goals

- Streamline car, rental, and payment tracking processes.
- Enable users to check car availability and book rentals online.
- Support digital payments through Stripe, eliminating cash-only dependency.
- Notify customer of overdue rentals and other key updates.
- Provide detailed records for rentals, users, cars, and payments for better management.



## Architecture

![architecture](https://snipboard.io/v4O3HU.jpg)



## Domain Models

User 
- Represents the core entity for both customers and managers.
- Stores user profile data like email, name, and authentication details.
- Facilitates secure access to the platform and interactions with rentals, payments, and notifications.
  
Role
- Defines the user's authorization level within the system.
- Provides clear separation of permissions for Managers and Customers.

Car 
- The central asset of the car-sharing service, representing the vehicles available for rent.
- Provides detailed information about available cars, their types, pricing, and inventory status.

Rental 
- Represents a booking made by a user to rent a car for a specified duration.
- Tracks the start and end dates of rentals, as well as whether the car was returned on time.

Payment
- Handles financial transactions for rentals and overdue fines.
- Integrates with Stripe API for secure payment processing.

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



## Core Functionalities

Authentication
- Register Users: POST /register.
- Login: POST /login to generate JWT tokens.
  
Car Management
- Add Cars: POST /cars (Admin only).
- View Cars: GET /cars (Available to all).
- Update Cars: PUT/PATCH /cars/{id} (Admin only).
- Delete Cars: DELETE /cars/{id} (Admin only).

Rental Management
- Rent Cars: POST /rentals (Inventory decreases by 1).
- View Rentals: GET /rentals with filters (user_id, is_active).
- Return Cars: POST /rentals/{id}/return (Inventory increases by 1).

Payments
- Create Payment: POST /payments (Stripe session creation).
- View Payments: GET /payments (Filter by user).
- Handle Stripe Success: GET /payments/success/{id}.
- Handle Stripe Cancel: GET /payments/cancel/{id}.

Telegram Notifications
- Rental Updates: Notify admins about overdue or successful rentals.
- Payment Updates: Notify about successful payments.
- Daily Overdue Check: Scheduled task to alert on overdue rentals.



## Technology Stack
This project uses cutting-edge technologies to ensure high performance and a smooth user experience:

Core Application Framework
- Spring Boot: A stable and scalable backend framework.
- Spring Security: For secure authentication and authorization.
- Spring Data JPA: Simplifies database operations.

Database Management
- SQL Database: Reliable and efficient storage.
- Liquibase: Manages database version control and migrations.
- Data Transformation and Communication
- MapStruct: Automates data mapping.
- JWT: Provides secure session management.

Development and Documentation
- Swagger: API documentation and testing.
- GlobalExceptionHandler: Manages exceptions gracefully.

Notifications and Payment Integration
- Telegram Bot API: Sends real-time notifications about rentals, payments,
- and overdue returns to administrators.
- Stripe API: Facilitates secure and efficient online payments for rentals and fines.

Deployment and Scalability
- Docker: For containerized deployment.



# How to Run the Application Locally?
Follow these steps to set up and run the application on your local machine. 
The application is containerized using Docker, making it easy to deploy and manage.

### Prerequisites

Before you begin, ensure you have the following installed:

1. Docker: Install Docker Desktop from [Docker's official website](https://www.docker.com/products/docker-desktop).
2. Java (JDK 17 or later): Download from [Oracle](https://www.oracle.com/java/technologies/javase-downloads.html) or [OpenJDK](https://openjdk.org/).
3. Git: Install Git from [Git's official website](https://git-scm.com/).
4. Postman (optional): For testing the API, download [Postman](https://www.postman.com/downloads/).

### Steps to Set Up and Run 

1. Clone the Repository:
   `git clone https://github.com/your-username/your-repo-name.git && cd your-repo-name`

3. Build the Application: Use Maven to build the application:
   `mvn clean package`

4. Start Docker Containers:
   Make sure Docker is running on your system, then execute the following command to start the application along with its dependencies (e.g., database):
   `docker-compose up --build`

5. Stop the Application: To stop the application and its containers, use:
   `docker-compose down`

### Additional Notes 
- Database Configuration: The application uses Liquibase for database version control. Ensure the database container is started properly as defined in the docker-compose.yml file.
- Environment Variables: Modify the .env file (if present) or application.yml to adjust environment-specific configurations (e.g., database credentials, port numbers).

### Testing the API with Postman
- Import the [Postman collection](https://www.postman.com/supply-astronomer-36769183/car-sharing-service-management-system/collection/00jmifh/car-sharing-service-management-system?action=share&source=copy-link&creator=34116334) to test API endpoints.
- Set `localhost:8088` as the base URL for all requests.

### With these steps, your application should be up and running locally in no time!


# Demonstration

While most are familiar with the general idea of an online bookstore, this project showcases its functionality through Postman.
[**CLICK HERE**](https://olexsandradorofeieiva.wistia.com/medias/vxox3ok1fa) to explore the world of possibilities where every book finds its reader!
