# 🏨 Hotel Reservation System named as StayEase.
A robust backend REST API for managing hotel reservations built with
Java Spring Boot. This project was developed as part of the 
CodeAlpha Java Programming Internship.

----------------------------------------------------------------------------

## 👨‍💻 Developer

**Swochhandita Ghimire**
- GitHub: (https://github.com/Swochhandita)
- LinkedIn: (www.linkedin.com/in/swochhandita112)

------------------------------------------------------------------------------

## 📋 Table of Contents

- [Overview]
- [Features]
- [Tech Stack]
- [Project Structure]
- [Getting Started]
- [API Endpoints]
- [Database Schema]
- [Authentication]
- [PDF Export]

----------------------------------------------------------------------------

## 📖 Overview

This project is a backend hotel reservation system that allows guests
to browse hotels, book rooms, make payments and export their
reservation details as PDF. Admins can manage hotels, rooms,
reservations, and process refunds.

------------------------------------------------------------------------------

## ✨ Features

### Guest
- Register and login with JWT authentication
- Browse hotels and search by city
- View available rooms by hotel
- Create and cancel reservations
- Simulate payment via CASH, CARD, eSewa, or Khalti
- Export reservation details as PDF
- View own profile and reservation history

### Admin
- Full CRUD for hotels and rooms
- Manage room status (AVAILABLE / MAINTENANCE)
- View and manage all reservations
- Update reservation status
- Process payment refunds
- View all registered users

---------------------------------------------------------------------------------------

## 🛠 Tech Stack

| Technology        | Purpose                          |
|-------------------|----------------------------------|
| Java 17           | Programming language             |
| Spring Boot 4.x   | Backend framework                |
| Spring Security   | Authentication and authorization |
| JWT (JJWT 0.12.5) | Stateless token-based auth       |
| Spring Data JPA   | Database ORM                     |
| Hibernate         | JPA implementation               |
| MySQL 8           | Relational database              |
| Liquibase         | Database migration                |
| MapStruct         | DTO ↔ Entity mapping             |
| Lombok            | Boilerplate reduction            |
| iText 7           | PDF generation                   |
| Maven             | Build tool                       |
| SpringDoc OpenAPI | Swagger UI documentation         |
                                            
---------------------------------------------------------------------------------

## 📁 Project Structure
src/main/java/com/codealpha/hotel_management_system/
├── config/
│   ├── jwt/
│   │   ├── JwtUtils.java
│   │   ├── JwtAuthFilter.java
│   │   └── CustomUserDetails.java
│   └── SecurityConfig.java
    |__ SwaggerConfig.java
    |__ DataSeeder.java
├── constant/
│   └── ApiConstant.java
├── controller/
│   ├── BaseController.java
│   ├── AuthController.java
│   ├── UserController.java
│   ├── HotelController.java
│   ├── RoomController.java
│   ├── ReservationController.java
│   └── PaymentController.java
├── dto/
│   ├── request/
│   │   ├── RegisterRequest.java
│   │   ├── LoginRequest.java
│   │   ├── HotelRequest.java
│   │   ├── RoomRequest.java
│   │   ├── RoomStatusUpdateRequest.java
│   │   ├── ReservationRequest.java
│   │   ├── ReservationStatusUpdateRequest.java
│   │   └── PaymentRequest.java
│   └── response/
│       ├── ApiResponse.java
│       ├── AuthResponse.java
│       ├── UserResponse.java
│       ├── HotelResponse.java
│       ├── RoomResponse.java
│       ├── ReservationResponse.java
│       ├── PaymentResponse.java
│       └── PagedResponse.java
├── entity/
│   ├── User.java
│   ├── Hotel.java
│   ├── Room.java
│   ├── Reservation.java
│   ├── Payment.java
│   └── enums/
│       ├── Role.java
│       ├── RoomType.java
│       ├── RoomStatus.java
│       ├── ReservationStatus.java
│       ├── PaymentMethod.java
│       └── PaymentStatus.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   ├── ApiException.java
│   ├── DuplicateResourceException.java
│   ├── RoomNotAvailableException.java
│   └── UnauthorizedException.java
├── mapper/
│   ├── UserMapper.java
│   ├── HotelMapper.java
│   ├── RoomMapper.java
│   ├── ReservationMapper.java
│   └── PaymentMapper.java
├── repository/
│   ├── UserRepository.java
│   ├── HotelRepository.java
│   ├── RoomRepository.java
│   ├── ReservationRepository.java
│   └── PaymentRepository.java
├── service/
│   ├── AuthService.java
│   ├── UserService.java
│   ├── HotelService.java
│   ├── RoomService.java
│   ├── ReservationService.java
│   ├── PaymentService.java
│   ├── PdfService.java
│   ├── CustomUserDetailsService.java
│   └── impl/
│       ├── AuthServiceImpl.java
│       ├── UserServiceImpl.java
│       ├── HotelServiceImpl.java
│       ├── RoomServiceImpl.java
│       ├── ReservationServiceImpl.java
│       └── PaymentServiceImpl.java
└── util/
└── ResponseUtil.java
src/main/resources/
├── application.yml
├── application-dev.yml
└── db/
└── changelog/
├── changelog-master.yaml
└── changesets/
├── 01_create_table_users.sql
├── 02_create_table_hotels.sql
├── 03_create_table_rooms.sql
├── 04_create_table_reservations.sql
└── 05_create_table_payments.sql

--------------------------------------------------------------------------------------

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- MySQL 8
- Maven 3.x
- IntelliJ IDEA (recommended)

### 1. Clone the repository

```bash
git clone https://github.com/Swochhandita/CodeAlpha_Hotel-Management-System.git
cd CodeAlpha_HotelManagementSystem
```

### 2. Create the database

```sql
CREATE DATABASE hotel_management_db;
```

### 3. Configure `application-dev.yml`

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hotel_management_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    username: your_mysql_username
    password: your_mysql_password
```

### 4. Run the application

```bash
mvn spring-boot:run
```

Liquibase will automatically create all tables on first run.

### 5. Default Admin Credentials

A default admin is created automatically on first startup:
- Email: `admin@stayease.com`
- Password: `admin123`

> Change these credentials after first login in production.

### 6. Test the API

Import the Postman collection or use any REST client.

First register an admin user then start testing:
POST /api/v1/auth/register
{
"name": "Admin User",
"email": "admin@stayease.com",
"password": "password123",
"phone": "9800000000"
}

---

## 📡 API Endpoints

### Auth — Public
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/auth/register` | Register new user |
| POST | `/api/v1/auth/login` | Login and get JWT token |

### Users
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| GET | `/api/v1/users/list` | ADMIN | Get all users |
| GET | `/api/v1/users/view/{id}` | ADMIN | Get user by ID |
| GET | `/api/v1/users/me` | Authenticated | Get own profile |
| DELETE | `/api/v1/users/delete/{id}` | ADMIN | Delete user |
| PATCH | `/api/v1/users/promote/{id}` | ADMIN | Promote user to admin |
### Hotels
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | `/api/v1/hotels/create` | ADMIN | Create hotel |
| GET | `/api/v1/hotels/list` | Public | Get all hotels |
| GET | `/api/v1/hotels/view/{id}` | Public | Get hotel by ID |
| GET | `/api/v1/hotels/search/city?city=Kathmandu` | Public | Search by city |
| PUT | `/api/v1/hotels/update/{id}` | ADMIN | Update hotel |
| DELETE | `/api/v1/hotels/delete/{id}` | ADMIN | Delete hotel |

### Rooms
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | `/api/v1/hotels/{hotelId}/rooms/create` | ADMIN | Add room |
| GET | `/api/v1/hotels/{hotelId}/rooms/list` | Public | Get all rooms |
| GET | `/api/v1/hotels/{hotelId}/rooms/view/{roomId}` | Public | Get room by ID |
| GET | `/api/v1/hotels/{hotelId}/rooms/available` | Public | Get available rooms |
| PUT | `/api/v1/hotels/{hotelId}/rooms/update/{roomId}` | ADMIN | Update room |
| PATCH | `/api/v1/hotels/{hotelId}/rooms/status/{roomId}` | ADMIN | Update room status |
| DELETE | `/api/v1/hotels/{hotelId}/rooms/delete/{roomId}` | ADMIN | Delete room |

### Reservations
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | `/api/v1/reservations/create` | Authenticated | Create reservation |
| GET | `/api/v1/reservations/list` | ADMIN | Get all reservations |
| GET | `/api/v1/reservations/view/{id}` | Authenticated | Get reservation by ID |
| GET | `/api/v1/reservations/me` | Authenticated | Get own reservations |
| PATCH | `/api/v1/reservations/status/{id}` | ADMIN | Update status |
| PATCH | `/api/v1/reservations/cancel/{id}` | Authenticated | Cancel reservation |
| GET | `/api/v1/reservations/export/{id}` | Authenticated | Export PDF |

### Payments
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | `/api/v1/payments/pay/{reservationId}` | Authenticated | Process payment |
| GET | `/api/v1/payments/view/{paymentId}` | Authenticated | Get payment by ID |
| GET | `/api/v1/payments/reservation/{reservationId}` | Authenticated | Get by reservation |
| PATCH | `/api/v1/payments/refund/{paymentId}` | ADMIN | Refund payment |

----------------------------------------------------------------------------------------

## 🗄 Database Schema

### Entity Relationships
User ──────────── Reservation ──────────── Room ──────────── Hotel
│
└──────────── Payment

### Tables

| Table | Description |
|-------|-------------|
| `users` | Registered users with roles GUEST or ADMIN |
| `hotels` | Hotel listings with city, rating, contact |
| `rooms` | Rooms belonging to hotels with type and status |
| `reservations` | Bookings linking users to rooms with dates |
| `payments` | Payment records for reservations |

---

## 🔐 Authentication

This project uses **JWT (JSON Web Token)** based stateless authentication.

### How it works

Client registers or logs in
Server returns a JWT token
Client sends token in every request header:
Authorization: Bearer <token>
Server validates token on every request
Token expires after 24 hours

## 📚 API Documentation

Swagger UI is available at: http://localhost:8080/swagger-ui/index.html

**To test secured endpoints:**
1. Login via `POST /api/v1/auth/login`
2. Copy the token from the response
3. Click **Authorize** button in Swagger UI
4. Enter `Bearer <your_token>`
5. Click **Authorize** — all requests will include the token


### Roles

| Role | Access |
|------|--------|
| `GUEST` | Browse hotels, make reservations, payments, export PDF |
| `ADMIN` | Full access — manage hotels, rooms, users, reservations |


### Promoting a user to admin

A default admin is created on startup. To add more admins:

1. Login as existing admin
2. Call `PATCH /api/v1/users/promote/{id}` with the guest's user ID
3. The guest is now promoted to admin
---

## 📄 PDF Export

Guests can export their reservation details as a PDF file.

**Endpoint:** `GET /api/v1/reservations/export/{reservationId}`

**PDF contains:**
- Reservation ID and status
- Guest name and email
- Hotel name and city
- Room number and type
- Check-in and check-out dates
- Number of nights
- Price breakdown
- Total amount in NPR

-----------------------------------------------------------------------------------------

## 💳 Payment Methods

StayEase supports Nepal-specific payment options:

| Method | Description |
|--------|-------------|
| `CASH` | Cash payment at hotel |
| `CARD` | Credit or debit card |
| `ESEWA` | Nepal digital wallet |
| `KHALTI` | Nepal digital wallet |

----------------------------------------------------------------------------------------

## 🏗 Room Types

| Type | Description |
|------|-------------|
| `STANDARD` | Basic room with essential amenities |
| `DELUXE` | Enhanced amenities and better view |
| `SUITE` | Premium multi-room with luxury amenities |

-------------------------------------------------------------------------------------------

## 📊 Reservation Status Flow
PENDING → CONFIRMED → COMPLETED
↓
CANCELLED

--------------------------------------------------------------------------------------------

## 🙏 Acknowledgements

- **CodeAlpha** — for the internship opportunity

-----------------------------------------------------------------------------------------------
