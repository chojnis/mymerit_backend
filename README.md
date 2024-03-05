# MyMerit - Backend

## Table of Contents

- [Requirements](#requirements)
- [Installation](#installation)
  - [Database](#database)
  - [Judge0](#judge0)
- [Usage](#usage)

## Requirements

- [JDK Development Kit 17](https://www.oracle.com/java/technologies/downloads/#java17)
- [Docker](https://www.docker.com/)

## Installation

### Database
1. ```cd database ```
2. ```docker-compose up -d```

**Note:** The database is available at [http://localhost:8081](http://localhost:8081). You need to create a database named _**mymerit**_.

### Judge0
1. ```cd judge0```
2. ```docker-compose up -d```

**Note:** The Judge0 API is available at [http://localhost:2358](http://localhost:2358).

## Usage
1. ```./gradlew clean build```
2. ```./gradlew bootRun``` or ```java -jar build/libs/my-merit-0.0.1-SNAPSHOT.jar```

**Note:** The application is available at [http://localhost:8080](http://localhost:8080). For testing, I recommend [Postman](https://www.postman.com/).
