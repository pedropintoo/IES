115304
# Lab 03 - Spring Boot, Spring Data JPA, and Docker

## Table of Contents
1. [Spring Framework vs Spring MVC vs Spring Boot](#spring-framework-vs-spring-mvc-vs-spring-boot)
2. [Accessing Database in Spring Boot using Spring Data JPA](#accessing-database-in-spring-boot-using-spring-data-jpa)
3. [Multilayer Applications: Exposing Data with REST Interface](#multilayer-applications-exposing-data-with-rest-interface)
4. [Dockerizing a Spring Boot Application with PostgreSQL](#dockerizing-a-spring-boot-application-with-postgresql)

---


# Spring Framework vs Spring MVC vs Spring Boot

> **Note:** Spring Boot does not compete with Spring or Spring MVC. It simplifies their usage.

---

### Spring Framework
- The key feature of the Spring Framework is **Dependency Injection** (or **IOC** — Inversion of Control).
- Dependency Injection is at the core of all Spring modules, facilitating loose coupling between components.

---

### Spring MVC
- Spring MVC provides a decoupled way to develop web applications.
- Concepts like **Dispatcher Servlet**, **ModelAndView**, and **View Resolver** make it easier to design web applications.

---

### Spring Boot Auto Configuration
- The issue with Spring Framework and Spring MVC is the extensive configuration required.
- Spring Boot simplifies this by:
  - Inspecting the **Frameworks** available on the CLASSPATH.
  - Analyzing existing **configurations**.
  - Providing **Auto Configuration**, which sets up the basic configuration based on the existing environment.

---

### Spring Boot Starter Projects
- **Starters** are pre-configured sets of dependencies to streamline development.
- Instead of manually configuring each dependency, you can use a starter to bring in related technologies easily.
  - For example: To work with **Spring** and **JPA**, you just include `spring-boot-starter-data-jpa` in your project.
  
- **Spring Boot Starter Web** bundles several essential components for web development, such as:
  - **Spring Core**: Core, Beans, Context, AOP
  - **Web MVC**: Spring MVC
  - **Jackson**: For JSON Binding
  - **Validation**: Hibernate Validator, Validation API
  - **Embedded Servlet Container**: Tomcat
  - **Logging**: Logback, SLF4J

- **Spring Boot Starter Parent**:
  - Provides consistent project configuration management.
  - Handles **dependency management** for your project.

---

### Spring Initializr: Create Spring Boot Projects at F1 Speed
- **Spring Initializr** is a web-based tool by Pivotal Software, Inc. for quickly bootstrapping Spring Boot projects.
- You can select dependencies and generate the project structure in just a few clicks.
- Some types of applications you can bootstrap:
  - Web Applications
  - RESTful Applications
  - Batch Applications

---

### Spring Boot and Embedded Servers
- Spring Boot includes pre-configured **embedded servers** such as:
  - **Tomcat** (default)
  - **Jetty**
  - **Undertow**

- You can switch to Jetty or Undertow easily by updating your project dependencies.

- Example: You can generate a Spring Boot application JAR with embedded **Tomcat**, allowing you to run your web app as a standard Java application.

---

### Spring Data
- **Spring Data** provides a consistent programming model for **data access**, while maintaining the characteristics of the underlying data store.
- It simplifies working with:
  - **Relational** and **Non-Relational Databases**
  - **Map-Reduce Frameworks**
  - **Cloud-based Data Services**

---

# Accessing Database in Spring Boot using Spring Data JPA

## Architecture Overview

1. **Frontend (View Layer)**
    - Thymeleaf templates:
      - Used for rendering the user interface (UI).
      - HTML files like index.html, add-user.html, and update-user.html manage the presentation.
      - Thymeleaf integrates with the backend to dynamically display and update data.

2. **Controller Layer (UserController)**
    - **UserController**:
      - Acts as the intermediary between the frontend (UI) and the data layer (repository).
      - Handles HTTP requests like GET, POST, and interacts with the user repository.
      - Controls user management operations such as:
          - `showUserList()`: Display all users.
          - `addUser()`: Handle form submission to add a new user.
          - `showUpdateForm()`: Display the user update form.
          - `updateUser()`: Handle the submission to update a user.
          - `deleteUser()`: Delete a user by ID.

3. **Service/Repository Layer (UserRepository)**
    - **UserRepository**:
      - Data access layer interacting with the database.
      - Extends `CrudRepository` to provide out-of-the-box CRUD operations.
      - Key methods:
          - `findAll()`: Retrieve all users.
          - `findById()`: Find a user by ID.
          - `save()`: Save or update a user.
          - `delete()`: Delete a user.

4. **Data Layer (Entity & Database)**
    - **User Entity**:
      - Mapped to the `tlb_user` table using the `@Entity` annotation.
      - Fields like `id`, `name`, and `email` correspond to table columns.
      - Uses validation annotations (`@NotBlank`) for business rule enforcement.

    - **H2 Database**:
      - In-memory relational database for runtime data storage.

5. **Spring Boot Framework**
    - Manages the application lifecycle, dependency injection, validation, and transaction management.

6. **Dependency Management**
    - **Spring Data JPA**: Implements JPA (Java Persistence API).
    - **Spring Web**: Supports RESTful web applications.
    - **Validation**: Data validation annotations, such as `@NotBlank`.



## How is the `userRepository` instantiated in the `UserController` class?

The `userRepository` is instantiated via dependency injection in the constructor of the `UserController` class. Spring automatically injects an instance of `userRepository` when the `UserController` is created, as `UserRepository` is marked with the `@Repository` annotation, making it a Spring-managed bean.

When the application starts, Spring scans for all repository interfaces (like `UserRepository`), detects that it extends `CrudRepository`, and automatically provides an implementation. This implementation is then injected into the `UserController` constructor.

```java
public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
}
```

---


## List the methods invoked in the `userRepository` object by the `UserController`. Where are these methods defined?

The methods invoked in the `userRepository` object within `UserController` are:

- **`findAll()`**: Used to fetch all users (in `showUserList()`).
- **`findById(Long id)`**: Used to retrieve a user by its ID (in `showUpdateForm()` and `deleteUser()`).
- **`save(User user)`**: Used to save or update a user (in `addUser()` and `updateUser()`).
- **`delete(User user)`**: Used to delete a user by the User entity (in `deleteUser()`).

These methods are not explicitly defined in the `UserRepository` interface but are inherited from the `CrudRepository` interface, which is part of Spring Data JPA.


## Where is the data being saved?

The data is saved in the H2 in-memory database that you configured as a dependency in the project. When you call `save()` on `userRepository`, JPA maps the `User` entity to the corresponding table in the database (`tlb_user`) and persists the data there. The in-memory H2 database stores data temporarily and will be erased once the application stops unless configured otherwise.


## Where is the rule for the "not empty" email address defined?

The rule for ensuring that the email address is not empty is defined in the `User` entity class using the `@NotBlank` annotation, which comes from the `jakarta.validation.constraints` package. This annotation ensures that when a user is created or updated, the email field cannot be blank.

```java
@NotBlank(message = "Email is mandatory")
private String email;
```

This rule is enforced by Spring's validation mechanism when the form is submitted. If the email field is empty, the validation error is triggered, and the user is redirected back to the form with the validation error message.

## Add Phone Number to User Entity

To add a new field for the phone number in the `User` entity, update the entity class to include a `phone` field. Use the `@NotBlank` annotation to enforce that the phone number is mandatory. Additionally, apply the `@Pattern` annotation to ensure that the phone number only contains valid characters, such as digits and an optional plus sign (+) at the beginning.

```java
@NotBlank(message = "Phone number is mandatory")
@Pattern(regexp = "^\\+?[0-9]*$", message = "Invalid phone number")
private String phone;
```

Next, include getter and setter methods for the phone number field in the `User` entity class to allow access and modification of this data.

```java
public String getPhone() {
    return phone;
}
public void setPhone(String phone) {
    this.phone = phone;
}
```

Finally, update the Thymeleaf templates (e.g., `add-user.html`, `update-user.html`, and `index.html`) to include the phone number field in the form, ensuring it is available for user input and display.

---

# Multilayer Applications: Exposing Data with REST Interface


### Docker Compose for MySQL Database

Define the MySQL service in a `docker-compose.yml` file to run a MySQL database as part of the application environment. Set up environment variables for root password, database name, username, and user password to initialize the database.

```yaml
services:
  mysql5:
    image: mysql/mysql-server:5.7
    container_name: mysql5
    environment:
      MYSQL_ROOT_PASSWORD: secret1
      MYSQL_DATABASE: demo
      MYSQL_USER: demo
      MYSQL_PASSWORD: secret2
    ports:
      - "33060:3306"
    restart: always
```

After defining the `docker-compose.yml`, start the database by running:

```bash
docker-compose up -d
```

## Employee Management System REST API Structure

### Key Components

1. **`RestapiMysqlApplication`**: Acts as the main entry point for the Spring Boot application. It starts and initializes the application by running `SpringApplication.run`.

2. **`EmployeeService` and `EmployeeServiceImpl`**: The service layer manages the core business logic. 
   - **`EmployeeService`**: An interface defining methods such as `createEmployee`, `getEmployeeById`, `getEmployeeByEmail`, and more.
   - **`EmployeeServiceImpl`**: Implements `EmployeeService` and interacts with `EmployeeRepository` to perform database operations, ensuring the separation of business logic from the data access layer.

    ```java
    public interface EmployeeService {
        Employee createEmployee(Employee employee);
        Employee getEmployeeById(Long employeeId);
        Employee getEmployeeByEmail(String email);
        List<Employee> getAllEmployees();
        Employee updateEmployee(Employee employee);
        void deleteEmployee(Long employeeId);
    }
    ```
    ```java
    @Override
    public Employee getEmployeeByEmail(String email) {
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(email);
        return optionalEmployee.get();
    }
    ``` 

3. **`EmployeeRepository`**: This repository extends `JpaRepository` and provides CRUD functionality for the `Employee` entity. It also defines a custom method, `findByEmail`, which allows searching for an employee by their email, using Spring Data JPA’s query creation from method names.

    ```java
    @Repository
    public interface EmployeeRepository extends JpaRepository<Employee, Long> {
        Optional<Employee> findByEmail(String email);
    }
    ```

4. **`EmployeeController`**: Exposes REST API endpoints for managing `Employee` entities, acting as the entry point to the API. Key operations include:
   - **Create Employee** (`POST /api/employees`): Adds a new employee to the system.
   - **Get Employee by ID** (`GET /api/employees/{id}`): Retrieves an employee using their unique identifier.
   - **Search by Email or Get All Employees** (`GET /api/employees?email=...`): Returns an employee by email if provided; otherwise, retrieves all employees.
      ```js
      GET /api/employees?email=someone@example.com
      ```

   - **Update Employee** (`PUT /api/employees/{id}`): Updates an employee's details based on their ID.
   - **Delete Employee** (`DELETE /api/employees/{id}`): Deletes an employee by their ID.

5. **`Employee` Entity**: Represents the `Employee` table in the database, with attributes like `id`, `name`, and `email`. The `email` field is marked as unique, ensuring that each employee has a distinct email address.

6. **`ResourceNotFoundException`**: Custom exception thrown when an entity (e.g., an employee) is not found. It is handled globally, allowing the API to return a 404 Not Found error in response.

---

## Employee API Interaction Flow using Postman

### Initial Employee List Retrieval

In the first step, we request the full list of employees stored in the system. At this point, two employees are present: Pedro and Danilo.

![alt text](<data/postman1.png>)

### Retrieve a Specific Employee by ID

We then query the employee with ID 1, which returns details about Pedro.

![alt text](<data/postman2.png>)

### Adding a New Employee

Next, we create a new employee by making a POST request with outro_user as the name and email@ua.pt as the email. The system assigns this new employee the ID 52.

![alt text](<data/postman3.png>)

### Retrieving the Updated Employee List

After adding the new employee, we request the full list again. Now the employee list includes outro_user along with the original two employees.

![alt text](<data/postman4.png>)

### Retrieve a Specific Employee by Email

We then query the employee with Email email@ua.pt, which returns details about the recently added employee.

![alt text](<data/postman5.png>)

### Updating an Employee

We then update the newly added employee (ID 52). We change the name to nome correto and the email to fabio@ua.pt. The update is reflected in the response.

![alt text](<data/postman6.png>)

### Deleting an Employee

Finally, we delete the employee with ID 52. The system responds with confirmation that the deletion was successful.

![alt text](<data/postman7.png>)

### Final Employee List Retrieval

To confirm the deletion, we retrieve the list again. The employee outro_user (ID 52) is no longer in the list, leaving only the initial two employees.

![alt text](<data/postman8.png>)

### Error Handling: Resource Not Found

If we attempt to retrieve an employee with an ID that does not exist (e.g., ID 33), the system responds with a 404 Not Found error message.

![alt text](<data/postmanE1.png>)

### Error Handling: Invalid Endpoint

If we attempt to access an invalid endpoint (e.g., /api/not_found_endpoint), the system responds with a 500 Internal Server Error message.

![alt text](<data/postmanE2.png>)

---

## Dockerizing a Spring Boot Application with PostgreSQL

### Project Structure

- **Entities**: Define database tables, such as `Movie` and `Quote`.
- **DTOs**: Data Transfer Objects, such as `QuoteRequest`, used to transfer data between layers.
- **Repositories**: Interfaces for CRUD operations and custom queries.
- **Services**: Implement business logic for movie and quote management.
- **Controllers**: Expose REST endpoints for API interaction.
- **Exceptions**: Custom exceptions, like `ResourceNotFoundException`, for handling errors gracefully.

### Endpoints
  - **`GET /api/movies`**: Retrieve all movies.
  - **`GET /api/quotes`**: Retrieve all quotes or quotes by movie ID.
  - **`GET /api/quotes?movieId=*`**: Retrieve quotes by movie ID.
  - **`GET /api/random/quote`**: Retrieve a random quote.
  - **`POST /api/movies`**: Create a new movie.
  - **`POST /api/quotes`**: Create a new quote for a specified movie.

### Dockerization Steps

#### Dockerfile

Create a `Dockerfile` to containerize the Spring Boot application. Use the `openjdk:17-jdk-alpine` image as the base, copying the application’s JAR file into the container and setting an `ENTRYPOINT` to run the JAR file when the container starts.

```Dockerfile
FROM openjdk:17-jdk-alpine
COPY target/quotes-0.0.1-SNAPSHOT.jar quotes-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/quotes-0.0.1-SNAPSHOT.jar"]
```

#### Docker Compose Configuration


1. Define environment variables in a `.env` file for PostgreSQL configuration, including database name, user, and password.

    ```.env
    POSTGRES_USER=quotes
    POSTGRES_PASSWORD=secret2
    POSTGRES_DB=quotes
    POSTGRES_LOCAL_PORT=5432
    POSTGRES_DOCKER_PORT=5432

    QUOTES_LOCAL_PORT=8080
    QUOTES_DOCKER_PORT=8080
    ```

2. Create a `docker-compose.yml` file to set up services for PostgreSQL and the Spring Boot application. Ensure that database and application ports are correctly mapped and that each service uses the environment variables for configuration.

    ```yaml
    services:
      postgres:
        image: postgres:12
        container_name: postgres
        env_file: ./.env
        environment:
          POSTGRES_USER: ${POSTGRES_USER}
          POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
          POSTGRES_DB: ${POSTGRES_DB}
        ports:
          - "${POSTGRES_LOCAL_PORT}:${POSTGRES_DOCKER_PORT}"
        restart: always

      quotes:
        depends_on: 
          - postgres
        build: .
        env_file: ./.env
        ports:
          - "${QUOTES_LOCAL_PORT}:${QUOTES_DOCKER_PORT}"
        environment:
          SPRING_APPLICATION_JSON: >-
            {
              "spring.datasource.url"  : "jdbc:postgresql://postgres:${POSTGRES_DOCKER_PORT}/${POSTGRES_DB}",
              "spring.datasource.username" : "${POSTGRES_USER}",
              "spring.datasource.password" : "${POSTGRES_PASSWORD}",
              "spring.jpa.properties.hibernate.dialect" : "org.hibernate.dialect.PostgreSQLDialect",
              "spring.jpa.hibernate.ddl-auto" : "update"
            }
        stdin_open: true
        tty: true
    ```

### Running the Application with Docker

1. **Build the JAR file**:
   Package the application to create a JAR file that can be run inside a Docker container.
    ```bash
    mvn clean package
    ```

2. **Start the application with Docker Compose**:
   Use Docker Compose to build and launch both the PostgreSQL and Spring Boot services as defined in the `docker-compose.yml` file. This will initialize the application and the database in a single command.
    ```bash
    docker-compose up --build
    ```
    Result:

    ![alt text](<data/docker1.png>)

3. **Access the Application**:

   Once the application is up and running, you can access it at `http://localhost:8080` or the designated port you specified.

4. **View Logs**:

   To monitor the real-time logs for the application, you can view output from the `quotes` container. This allows you to keep track of application behavior and debug as necessary.
    ```bash
    docker-compose logs -f quotes
    ```

## Final Result (Postman Requests)

### 1. Retrieve All Movies

![alt text](<data/postman21.png>)

### 2. Retrieve All Quotes

![alt text](<data/postman22.png>)

### 3. Retrieve All Quotes by Movie ID

![alt text](<data/postman23.png>)

### 4. Retrieve Random Quote

![alt text](<data/postman24.png>)

### 5. Add a New Quote for a Movie

![alt text](<data/postman25.png>)

### 6. Retrieve All Quotes for the Movie

![alt text](<data/postman26.png>)

### 7. Add a New Movie

![alt text](<data/postman27.png>)

### 8. Retrieve All Movies (Updated)

![alt text](<data/postman28.png>)


# References

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring Data JPA Documentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#reference)
- [Spring Initializr](https://start.spring.io/)
- [Postman](https://www.postman.com/)
- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)

