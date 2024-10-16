# Purchase Order Service

This is a Spring Boot application for managing purchase orders. It allows you to create, read, update, and delete purchase orders and their details.

## Requirements

- Java 17 or higher
- Maven 3.6 or higher

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/hakimamarullah/purchase-order-service.git
cd purchase-order-service
```

### Build the Project

Navigate to the project directory and run:

```bash
mvn clean install
```

### Running the Application

To run the application, use the following command:

```bash
mvn spring-boot:run
```

The application will start on the default port `8080`. You can change the port by modifying the `application.properties` file:

```properties
server.port=8081
```

### Accessing the API

Once the application is running, you can access the REST API at:

```
http://localhost:8080
```

### Swagger UI

The application includes Swagger UI for API documentation. You can access it at:

```
http://localhost:8080/swagger-ui/index.html
```


### Testing

You can use tools like Postman or cURL to test the API endpoints. Here's an example using cURL:

```bash
curl -X GET http://localhost:8080/api/v1/purchase-orders
```

## Conclusion

This service provides a simple and efficient way to manage purchase orders.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

