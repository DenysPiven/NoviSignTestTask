# NoviSign Test Task Documentation

## **Overview**
The NoviSign Test Task is a Java Spring Boot application created to showcase expertise in modern Java technologies and practices. It allows users to manage images and slideshows while implementing key functionalities required for a production-level application.

---

## **Features**

### **Core Requirements**
1. **Backend Development**
    - Implement a Java Spring Boot application with the following RESTful APIs:
        - Add, delete, and search images.
        - Create and delete slideshows.
        - Retrieve slideshow order.
        - Record proof-of-play events.

2. **Data Storage**
    - Use PostgreSQL for persistent data storage.

3. **Modern Java Techniques**
    - Adhere to the latest Java best practices.

4. **Error Handling**
    - Return standardized error responses.

5. **Testing**
    - Write unit tests using JUnit 5.

### **Advanced Features**
1. **Event-Driven Architecture**
    - Use Spring EventPublisher for logging significant API actions.

2. **Containerization**
    - Create a Dockerfile and docker-compose.yml for containerized deployment.

---

## **Technologies Used**
- **Java**: JDK 21
- **Spring Boot**: Framework for building the application.
- **PostgreSQL**: Database for persistent data storage.
- **Swagger**: API documentation.
- **Docker**: Containerization.
- **JUnit 5**: Testing framework.

---

## **Setup Instructions**

### **1. Prerequisites**
- Java 21 or later
- Maven
- Docker and Docker Compose
- PostgreSQL

### **2. Clone the Repository**
```bash
git clone https://github.com/DenysPiven/NoviSignTestTask.git
cd NoviSignTestTask
```

### **3. Configure the Database**
1. Ensure PostgreSQL is running locally or via Docker.
2. Update `application.yml` with your database credentials:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/slideshow_db
    username: slideshow_user
    password: password
```

### **4. Run the Application**
#### **Using Maven**
```bash
mvn spring-boot:run
```

#### **Using Docker**
1. Build the Docker image:
   ```bash
   docker-compose up --build
   ```
2. Access the application at `http://localhost:8080`.

---

## **API Endpoints**

### **Image Management**
1. **Add a new image**
    - **Endpoint**: `POST /images/add`
    - **Description**: Adds a new image with a URL and duration.
    - **Request Body**:
      ```json
      {
        "url": "https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg",
        "duration": 5
      }
      ```

2. **Delete an image**
    - **Endpoint**: `DELETE /images/delete/{id}`
    - **Description**: Deletes an image by its ID.

3. **Search images**
    - **Endpoint**: `GET /images/search`
    - **Description**: Searches for images by keyword in their URL.
    - **Query Parameter**: `keyword` (string)

4. **Get image by ID**
    - **Endpoint**: `GET /images/{id}`
    - **Description**: Retrieves a single image by its ID.

### **Slideshow Management**
1. **Add a new slideshow**
    - **Endpoint**: `POST /slideshows/add`
    - **Description**: Creates a new slideshow with an array of images containing URLs and durations.
    - **Request Body**:
      ```json
      {
        "images": [
          {
            "url": "https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg",
            "duration": 5
          },
          {
            "url": "https://gratisography.com/wp-content/uploads/2024/10/gratisography-cool-cat-800x525.jpg",
            "duration": 3
          }
        ]
      }
      ```

2. **Delete a slideshow**
    - **Endpoint**: `DELETE /slideshows/delete/{id}`
    - **Description**: Deletes a slideshow by its ID.

3. **Get slideshow order**
    - **Endpoint**: `GET /slideshows/{id}/slideshowOrder`
    - **Description**: Retrieves the order of images in a slideshow by its ID.

4. **Proof of play**
    - **Endpoint**: `POST /slideshows/{id}/proof-of-play/{imageId}`
    - **Description**: Records an event when an image is displayed in a slideshow.

---

## **Database Schema**

### **Tables**
1. **Images**:
    - `id`: Primary Key
    - `url`: URL of the image
    - `duration`: Duration in seconds
    - `created_at`: Timestamp

2. **Slideshows**:
    - `id`: Primary Key
    - `images`: List of images with URLs and durations

3. **Event Logs**:
    - `id`: Primary Key
    - `event_type`: Type of event (e.g., IMAGE_ADDED, SLIDESHOW_DELETED)
    - `message`: Description of the event
    - `timestamp`: Time of the event

---

## **Testing**
1. Unit tests:
    - Run using Maven:
      ```bash
      mvn test
      ```
2. Integration tests:
    - Ensure H2 database is configured for testing.

---

## **Contact**
For any issues or queries, please reach out to [your-email@example.com].

