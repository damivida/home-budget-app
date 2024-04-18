# home-budget-app

### Overview
This is a Spring Boot application named home-budget-app which serves as a REST API for managing home budgets.

### Prerequisites
- Java 17
- Maven
- Docker (optional, for running PostgreSQL and pgAdmin)

### DB Configuration
Uncomment wanted DB configuration in properties. It can be configured to use either PostgreSQL or H2 as the database.

### Running with Docker
To run PostgreSQL and pgAdmin, go to env in root folder and use:
```bash
docker-compose up -d
```
### Running the Application
To build and run the application, go to root folder and use:
```bash
mvn clean install
mvn spring-boot:run
```
### Access API documentation and Db
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- For H2 Console (if using H2 Database): http://localhost:8080/h2-console
- For PgAdmin: http://localhost:15432/browser/
