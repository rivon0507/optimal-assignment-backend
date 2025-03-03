
# Optimal Assignment Backend

This is the backend service for the **Optimal Assignment Problem Solver**. It provides a REST API for solving assignment problems, utilizing an efficient algorithm to compute the optimal solution. The results are processed asynchronously, and intermediate steps are stored in **Redis** for real-time updates.

## Features

- Solve **Assignment Problems** using a provided algorithm.
- **Asynchronous** job processing.
- **Redis** for caching job states and intermediate results.
- API provides **real-time updates** on the solving process.
- Persist final results to **MariaDB**.

## Technologies Used

- **Spring Boot**: Framework for building the backend.
- **Spring Data JPA**: For ORM-based database interaction.
- **Spring Data Redis**: For caching job states and intermediate results.
- **MariaDB**: For persisting the final results.
- **Java 21+**: Java version for the backend logic.
- **Gradle**: Build automation tool.
- **Redis**: Caching solution.
- **Spring Web**: For RESTful APIs.

## Prerequisites

To run the backend locally, make sure you have the following installed:

- **Java 21** or later.
- **MariaDB** (if you choose to persist results).
- **Redis** (for caching and job data storage).

## Getting Started

### 1. Fork the repository

### 2. Clone the Repository

```bash
git clone https://github.com/yourusername/optimal-assignment-backend.git
cd optimal-assignment-backend
```

### 3. Build the Project

Build the project using Gradle:

```bash
./gradlew build
```

### 4. Set Up Redis and MariaDB (Optional)

### 5. Run the Application

Run the backend application:

```bash
./gradlew bootRun
```

By default, the backend runs on `http://localhost:8080`.

### 6. Access the API

Once the backend is up and running, you can access the API endpoints at `http://localhost:8080`.

### 7. Persist Final Results (Optional)

If you want to persist the final results to MariaDB, configure the **application.properties** with your database connection details.

## Tests

Unit tests for different components (like the service, controller, and repository) are available. To run the tests:

```bash
./gradlew test
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
