# Smart Campus Sensor & Room Management API

This project implements a RESTful API using JAX-RS (Jersey) to manage rooms, sensors, and sensor readings in a smart campus environment.

The system runs on an embedded Grizzly server and uses an in-memory data store to simulate real-world operations. The API demonstrates proper REST design, validation, error handling, filtering, and resource relationships.

---

## 🎥 Demo Video

(Insert your video link here)

---

## 📡 API Overview

Base URL:
http://localhost:8080/api/v1

### Core Endpoints

- GET / → API discovery  
- GET /rooms → List all rooms  
- GET /rooms/{id} → Get a specific room  
- GET /rooms/{id}/sensors → Sensors inside a room  
- DELETE /rooms/{id} → Delete a room  

- GET /sensors → List sensors (supports filtering)  
- GET /sensors/{id} → Get a sensor  
- POST /sensors → Create sensor  
- PUT /sensors/{id} → Update sensor  
- DELETE /sensors/{id} → Delete sensor  

- POST /sensors/{id}/readings → Add reading  
- GET /sensors/{id}/readings → Get readings  
- GET /sensors/{id}/readings/{readingId} → Get specific reading  

- GET /diagnostics/failure-test → Simulated 500 error  

---

## ⚙️ Build and Run

Run the following command:

mvn clean compile exec:java

The API will start at:
http://localhost:8080/api/v1

---

## 🧱 Design Notes

- The API follows a hierarchical structure:
  rooms → sensors → readings  

- HATEOAS links are included in responses to allow navigation between related resources.

- All responses follow a consistent JSON format including:
  status, message, data, and links.

- Data is stored in-memory using HashMap and ArrayList for simplicity.

---

## 🚧 Business Rules

- A room cannot be deleted if it has sensors → 409 Conflict  
- A sensor must be linked to an existing room → 422 Unprocessable Entity  
- Sensors in MAINTENANCE or OFFLINE cannot accept readings → 403 Forbidden  
- Duplicate sensor IDs are not allowed → 409 Conflict  

---

## 🧪 Example Requests

Get all rooms:
curl -i http://localhost:8080/api/v1/rooms

Create a sensor:
curl -i -X POST http://localhost:8080/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{"id":"TEMP-001","type":"Temperature","status":"ACTIVE","roomId":"LIB-301"}'

Add a reading:
curl -i -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings \
-H "Content-Type: application/json" \
-d '{"value":23.5}'

---

## ❗ Error Handling

| Code | Description |
|------|------------|
| 400 | Invalid input |
| 403 | Business rule violation |
| 404 | Resource not found |
| 405 | Method not allowed |
| 409 | Conflict |
| 422 | Invalid linked resource |
| 500 | Server error |

---

## 🧠 Key Features

- RESTful API design  
- Nested resource handling  
- Filtering with query parameters  
- Centralized exception handling  
- Request/response logging  
- Consistent response structure  

---

## 👨‍💻 Author

Thulnith Perera  
Student ID: w2120618  
