# Smart Campus Sensor & Room Management API

This project implements a RESTful API using JAX-RS (Jersey) to manage rooms, sensors, and sensor readings in a smart campus environment.

The system runs on an embedded Grizzly server and uses an in-memory data store to simulate real-world operations. The API demonstrates proper REST design, validation, error handling, filtering, and resource relationships.

---

## Demo Video

(Insert your video link here)

---

## API Overview

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

## Design Notes

- The API follows a hierarchical structure:
  rooms → sensors → readings  

- HATEOAS links are included in responses to allow navigation between related resources.

- All responses follow a consistent JSON format including:
  status, message, data, and links.

- Data is stored in-memory using HashMap and ArrayList for simplicity.

---

## Business Rules

- A room cannot be deleted if it has sensors → 409 Conflict  
- A sensor must be linked to an existing room → 422 Unprocessable Entity  
- Sensors in MAINTENANCE or OFFLINE cannot accept readings → 403 Forbidden  
- Duplicate sensor IDs are not allowed → 409 Conflict  

---

## Example Requests

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

## Error Handling

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

## Key Features

- RESTful API design
- Nested resource handling
- Filtering with query parameters
- Centralized exception handling
- Request/response logging
- Consistent response structure

---

## Author

Thulnith Perera  
Student ID: w2120618  

---

# Coursework Report Answers

## 1.1 Resource Lifecycle

By default, JAX-RS resource classes are request-scoped, meaning a new instance is created for each request. Because of this, shared data is not stored inside resource classes. Instead, a centralized in-memory data store is used to maintain state across requests.

## 1.2 Hypermedia / HATEOAS

HATEOAS improves the API by including links in responses that guide the client to related resources. This reduces the need for hardcoded endpoints and makes the API easier to navigate and extend.

## 2.1 IDs Only vs Full Room Objects

Returning only IDs reduces payload size and improves performance when minimal data is needed. However, returning full objects provides more context and reduces the need for additional requests. In this API, full objects are used where necessary for better usability.

## 2.2 DELETE Idempotency

DELETE is considered idempotent because repeating the same delete request results in the same final state. Once a resource is removed, further DELETE requests do not change the outcome, even if they return a 404.

## 3.1 @Consumes(MediaType.APPLICATION_JSON)

This annotation ensures that the API only accepts JSON input. If another content type is sent, the request is rejected, ensuring consistency and proper validation of request bodies.

## 3.2 Why QueryParam Is Better For Filtering

Query parameters allow flexible filtering of collections without changing the resource structure. For example, /sensors?type=Temperature is clearer and more scalable than embedding filters in the path.

## 4.1 Sub-Resource Locator Benefits

Sub-resource locators allow nested resources to be handled separately. This keeps the code clean and organized by separating sensor logic from reading logic, improving maintainability.

## 4.2 Historical Data Consistency

Each sensor maintains its own list of readings. When a new reading is added, the sensor’s current value is updated immediately to reflect the latest data, ensuring consistency between summary and history.

## 5.2 Why 422 Is More Accurate Than 404

422 is used when the request is valid but cannot be processed due to logical issues, such as referencing a non-existent room. This is more accurate than 404, which indicates a missing endpoint.

## 5.4 Stack Trace Exposure Risks

Exposing stack traces can reveal internal implementation details such as class names, file paths, and system structure. This can be a security risk, so the API hides these details using a global exception handler.

## 5.5 Why Filters Are Better For Logging

Filters allow logging to be handled in one place instead of repeating it in every method. This ensures consistency and makes the system easier to maintain.
