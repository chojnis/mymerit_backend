# MyMerit - backend

## Requirements

- [JDK Development Kit 17](https://www.oracle.com/java/technologies/downloads/#java17)
- [Docker](https://www.docker.com/)

## Installation

### Database

1. Go to the **database** directory
```
cd database
```
2. Create and start a container
```
docker-compose up -d
```

**Note:** The database is available at [http://localhost:8081](http://localhost:8081). You need to create a database named _**mymerit**_.

### Judge0

1. Go to the **judge0** directory
```
cd judge0
```
2. Create and start a container
```
docker-compose up -d
```

**Note:** The Judge0 API is available at [http://localhost:2358](http://localhost:2358).

## Run the application

### Windows

1. Assemble and test this project
```
.\gradlew.bat build
```
2. Run this project as a Spring Boot application
```
.\gradlew.bat bootRun
```

### Linux / macOS

1. Assemble and test this project
```
./gradlew build
```
2. Run this project as a Spring Boot application
```
./gradlew bootRun
```

**Note:** The application is available at [http://localhost:8080](http://localhost:8080). For testing, I recommend [Postman](https://www.postman.com/).
