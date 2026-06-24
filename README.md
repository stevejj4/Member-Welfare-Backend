# Member Registration System

A Spring Boot REST API for managing Principal Member registrations as part of the Member welfare Data system.

## 🚀 Features
- **Register Members**: POST endpoint to save new member data and update member info
- **List Members**: GET endpoint to retrieve all registered members.
- **List Member, next of Kin, dependants**: GET endpoint to retrieve member complete details
- **Aggregate PMservice that orchestrates the business rule**
- **Data storage**: MySQL for data parsistance.
- **DTOs for data protection and specificity**

## 🔌 REST API Endpoints
### 1. Register a Member
**URL:** `http://localhost:8080/api/members/register`  
**Method:** `POST`  
**Description:** Creates a new Principal Member in the `SUN_Member` table.

### 2. Get member
**URL:** `http://localhost:8080/api/members`  
**Method:** `GET`  
**Description:** Get all Principal Members in the `SUN_Member` table.

***URL:**`http://localhost:8080/api/{principal_member_id}`
***Memthod:** `GET`
**Description:** Get individual Principal Members in the `from all tables` table.

### 3. Check Duplicate Member Details
**URL:** `http://localhost:8080/api/v1/members/exists?nationalId={value}&phoneNumber={value}`  
**Method:** `GET`  
**Auth:** Requires an authenticated user with member read or create permission.

**Description:**  
Checks whether a Principal Member National ID or phone number already exists before the frontend moves from Principal Details to Next of Kin.

This endpoint does **not** return any member details, names, IDs, or records. It only returns two boolean values using the `MemberExistsResponseDTO` response object.

**Example response:**
```json
{
  "nationalIdExists": true,
  "phoneNumberExists": false
}
```

**Validation behavior:**
- `nationalId` and `phoneNumber` are optional, but at least one must be provided.
- Values are trimmed before checking.
- If both are missing or blank, the API returns `400 Bad Request`.

**Performance note:**  
The backend uses efficient repository methods:
- `existsByNationalID(String nationalID)`
- `existsByPhoneNumber(String phoneNumber)`

Because `nationalID` and `phoneNumber` are unique fields, the database should have indexes for them. With indexes, each lookup is approximately **O(log n)**, so even with a very large table, such as `100,000,000` members, the database does not scan every row. Without indexes, the lookup would be **O(n)** and much slower.


## 🛠️ Tech Stack
- **Java 17**
- **Spring Boot 3.4.2**
- **Spring Data JPA** (Hibernate)
- **Mysql database**
- **Maven** (Build Tool)
- **Spring Security**

## 📦 Prerequisites
- **JDK 17** or higher
- **Maven**
- **Postman** (for API testing)
- **MySQL** (Data parsistance)
- **JPA**
- **Validations**

## 🏃 Getting Started
1. **Clone the repository**:
   ```bash
   git clone <https://github.com/stevejj4/Member-Welfare-Backend.git>
2. ### 🗄️ Database Access

   













