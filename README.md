# MyMerit - backend

## Requirements

- [JDK Development Kit 17](https://www.oracle.com/java/technologies/downloads/#java17)
- [Docker](https://www.docker.com/)

## Installation

### Database

Go to the `database` directory

```
cd database
```

Create and start a container

```
docker-compose up -d
```

**Note:** The database is available at [http://localhost:8081](http://localhost:8081).
To populate the database with data, run the `restore` script depending on the platform.

### Judge0

Go to the `judge0` directory

```
cd judge0
```

**Important:** Make sure the `judge0.conf` file has the control characters set to LF.

Create and start a container

```
docker-compose up -d
```

**Note:** The Judge0 API is available at [http://localhost:2358](http://localhost:2358).

## Run the application

### Windows

Assemble and test this project

```
.\gradlew.bat build
```

Run this project as a Spring Boot application

```
.\gradlew.bat bootRun
```

### Linux / macOS

Assemble and test this project

```
./gradlew build
```

Run this project as a Spring Boot application

```
./gradlew bootRun
```

**Note:** The application is available at [http://localhost:8080](http://localhost:8080). For testing, I recommend [Postman](https://www.postman.com/).