# Coursework Report Answers

## 1.1 Resource Lifecycle

In JAX-RS, resource classes are request-scoped by default, meaning a new instance is created for each incoming request. Because of this, it is not suitable to store shared data directly inside resource classes. In this project, shared data such as rooms, sensors, and readings is stored in a central in-memory data store instead. This ensures that all requests work with the same data while avoiding issues related to object lifecycle and concurrency.

---

## 1.2 Hypermedia / HATEOAS

HATEOAS is an important concept in RESTful design where the server returns links along with the data. These links help the client understand what actions are available next, such as navigating from a sensor to its readings or from a room to its sensors. This reduces the need to hardcode URLs on the client side and makes the API easier to use and extend over time.

---

## 2.1 IDs Only vs Full Room Objects

Returning only IDs can reduce the size of the response and improve performance when the client only needs references. However, returning full room objects provides more useful information such as name, capacity, and associated sensors. In this API, full objects are used because it makes the system easier to work with and reduces the need for additional API calls.

---

## 2.2 DELETE Idempotency

The DELETE operation is considered idempotent because sending the same request multiple times results in the same final state. Once a resource is deleted, it no longer exists, and repeating the DELETE request will not change anything further. In this implementation, a second DELETE request may return a 404 Not Found, but the overall system state remains unchanged.

---

## 3.1 @Consumes(MediaType.APPLICATION_JSON)

The @Consumes(MediaType.APPLICATION_JSON) annotation specifies that the API only accepts JSON input. If a client sends data in a different format, such as text or XML, the request will be rejected before reaching the resource method. This ensures consistency in how data is handled and simplifies validation within the API.

---

## 3.2 Why @QueryParam Is Better For Filtering

Using @QueryParam for filtering, such as /sensors?type=CO2, is more suitable because it represents a filtered view of the same collection. If filtering was placed in the path, it would look like a different resource. Query parameters also allow multiple filters to be combined easily, making them more flexible and aligned with RESTful design principles.

---

## 4.1 Sub-Resource Locator Benefits

The Sub-Resource Locator pattern helps keep the API structure clean and modular. In this project, sensor readings are handled in a separate SensorReadingResource instead of mixing all logic into one class. This separation improves readability and makes the system easier to maintain, especially when dealing with nested resources.

---

## 4.2 Historical Data Consistency

Each sensor maintains its own list of readings through the /sensors/{sensorId}/readings endpoint. When a new reading is added, the system immediately updates the sensor’s currentValue. This ensures that the sensor always reflects the latest reading while still keeping the full history available.

---

## 5.2 Why 422 Is More Accurate Than 404

The 422 Unprocessable Entity status is more appropriate when the request is valid but contains incorrect data, such as referencing a room that does not exist. A 404 Not Found usually means the endpoint itself is incorrect. In this case, the endpoint is valid, but the data cannot be processed, so 422 is a better choice.

---

## 5.4 Stack Trace Exposure Risks

Exposing stack traces can reveal sensitive internal details such as class names, file paths, and system structure. This information can be useful for attackers when trying to understand how the system works. To avoid this risk, the API uses a global exception handler to hide these details and return a clean error response.

---

## 5.5 Why Filters Are Better For Logging

Filters are useful for handling cross-cutting concerns like logging because they apply to all requests in a centralized way. In this project, a logging filter is used to log incoming requests and responses. This avoids repeating logging code in every endpoint and keeps the implementation clean and consistent.
