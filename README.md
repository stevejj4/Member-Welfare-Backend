# Member Registration System

A Spring Boot REST API for managing Principal Member registrations as part of the SUN Data system.

## 🚀 Features
- **Register Members**: POST endpoint to save new member data.
- **List Members**: GET endpoint to retrieve all registered members.
- **In-Memory Storage**: Uses H2 Database for rapid assessment and testing.

## 🔌 REST API Endpoints
### 1. Register a Member
**URL:** `http://localhost:8080/api/register`  
**Method:** `POST`  
**Description:** Creates a new Principal Member in the `SUN_Member` table.

### 2. Get member
**URL:** `http://localhost:8080/api/members`  
**Method:** `GET`  
**Description:** Get all Principal Members in the `SUN_Member` table.


## 🛠️ Tech Stack
- **Java 17**
- **Spring Boot 3.4.2**
- **Spring Data JPA** (Hibernate)
- **Mysql database**
- **Maven** (Build Tool)

## 📦 Prerequisites
- **JDK 17** or higher
- **Maven**
- **Postman** (for API testing)

## 🏃 Getting Started
1. **Clone the repository**:
   ```bash
   git clone <https://github.com/stevejj4/Member-Welfare-Backend.git>
2. ### 🗄️ Database Access

   




