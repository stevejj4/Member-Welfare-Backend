# Member Registration System

A Spring Boot REST API for managing Principal Member registrations as part of the Member welfare Data system.

## 🚀 Features
- **Member registration**: Register principal members, next of kin, and dependants.
- **Duplicate checks**: Fast National ID and phone number validation without exposing member records.
- **Location-aware access**: Members and groups are scoped by county, sub-county, and ward assignments.
- **Groups**: Create groups per ward and view group stats/members.
- **Transfers**: Transfer individual members between approved wards or group members between approved groups.
- **Admin user assignments**: Admins assign coordinators to working areas and facilitators to one or more wards.
- **Data storage**: MySQL with Spring Data JPA.
- **Security**: Spring Security with permission-based access control.

## 🔐 Area Access Rules

- **Admin**: Can view and manage all members and groups.
- **Coordinator**: Can view members and groups in the assigned county/sub-county working area.
- **Facilitator**: Can view members and groups only in assigned ward(s).
- Backend validates access; the frontend should not rely on UI filtering alone.

## 🔌 REST API Endpoints

### 1. Register a Member
**URL:** `http://localhost:8080/api/v1/members/register`  
**Method:** `POST`  
**Auth:** Requires `MEMBER_CREATE`.

Registers a principal member with next of kin and optional dependants.

Principal member location fields:
```json
{
  "registrationType": "INDIVIDUAL",
  "countyId": 1,
  "subCountyId": 1,
  "wardId": 1,
  "groupId": null
}
```

Registration rules:
- `countyId`, `subCountyId`, and `wardId` are required.
- `registrationType` is either `INDIVIDUAL` or `GROUP`.
- If `registrationType` is `GROUP`, `groupId` is required.
- The selected group must belong to the selected ward.
- The logged-in user must be allowed to work in the selected ward.

### 2. List Members
**URL:** `http://localhost:8080/api/v1/members`  
**Method:** `GET`  
**Auth:** Requires `MEMBER_READ`.

Returns members visible to the logged-in user:
- Admin sees all.
- Coordinator sees members in their assigned county/sub-county.
- Facilitator sees members in assigned ward(s).

### 3. Get Member Details
**URL:** `http://localhost:8080/api/v1/members/{id}`  
**Method:** `GET`  
**Auth:** Requires `MEMBER_READ`.

Returns full details for one member only if the logged-in user is allowed to access that member's ward.

### 4. Check Duplicate Member Details
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

### 5. Transfer a Member
**URL:** `http://localhost:8080/api/v1/members/{id}/transfer`  
**Method:** `PATCH`  
**Auth:** Requires `MEMBER_WRITE`.

Request body:
```json
{
  "wardId": 1,
  "registrationType": "GROUP",
  "groupId": 1
}
```

Transfer rules:
- Individual transfer requires `wardId` and `registrationType: "INDIVIDUAL"`.
- Group transfer requires `wardId`, `registrationType: "GROUP"`, and `groupId`.
- The selected group must belong to the selected ward.
- Only approved wards/groups are accepted.

### 6. Location Lookups

**Counties**  
`GET http://localhost:8080/api/v1/locations/counties`

**Sub-counties by county**  
`GET http://localhost:8080/api/v1/locations/sub-counties?countyId=1`

**Wards by sub-county**  
`GET http://localhost:8080/api/v1/locations/wards?subCountyId=1`

These endpoints use existing master data tables:
- `counties`
- `sub_counties`
- `wards`

### 7. Current User Assignment

**Assignment**  
`GET http://localhost:8080/api/v1/me/assignment`

**Approved wards**  
`GET http://localhost:8080/api/v1/me/wards`

These endpoints help the frontend show only the locations the logged-in user is allowed to use.

### 8. Groups

**Create group**  
`POST http://localhost:8080/api/v1/groups`

Request body:
```json
{
  "name": "Group Name",
  "countyId": 1,
  "subCountyId": 1,
  "wardId": 1
}
```

**List groups**  
`GET http://localhost:8080/api/v1/groups?wardId=1`

Returns groups the logged-in user can access for the selected ward.

**Group details and stats**  
`GET http://localhost:8080/api/v1/groups/{id}`

Example response:
```json
{
  "group": {
    "id": 1,
    "groupId": "GRP-000001",
    "name": "Group Name",
    "wardName": "Burat",
    "subCountyName": "Garbatulla Sub County",
    "countyName": "Isiolo"
  },
  "totalMembers": 12,
  "members": []
}
```

The `members` array contains the principal members in that group only if the logged-in user is allowed to access the group.

### 9. Admin User Area Assignment

Admin-created coordinators and facilitators can be assigned working areas.

Coordinator payload:
```json
{
  "firstName": "Jane",
  "lastName": "Doe",
  "email": "jane@example.com",
  "assignedRole": "COORDINATOR",
  "countyId": 1,
  "subCountyId": 1,
  "wardIds": []
}
```

Facilitator payload:
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "assignedRole": "FACILITATOR",
  "countyId": 1,
  "subCountyId": 1,
  "wardIds": [1, 2, 3]
}
```

Rules:
- Coordinator requires county and sub-county.
- Facilitator requires county, sub-county, and at least one ward.
- Wards must belong to the selected sub-county.
- The sub-county must belong to the selected county.


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

   













