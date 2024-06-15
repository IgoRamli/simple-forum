How to run:

1. Specify the following environment variables:
   - DB_HOST (e.g. localhost)
   - DB_PORT (e.g. 5432)
   - DB_NAME (e.g. simple_forum)
   - DB_USER (e.g. simple-forum-app)
   - DB_PASS
   - KEYCLOAK_HOST (e.g. localhost:8100)
   - KEYCLOAK_REALM (e.g. SimpleForumKeycloak)
2. Run `mvn clean compile spring-boot:run`

How to build as JAR:

### Running as JAR file

You can build the service as a jar file and running the service from there:

1. Ensure that all test files are running correctly by running `mvn test` and verify the result
2. Run `mvn install`
3. JAR file should appear in target folder as simple-forum-{version number}.jar

Environment variable works as usual, just like when you do mvn spring-boot:run
```commandline
KEYCLOAK_HOST=localhost KEYCLOAK_REALM=SimpleForumKeycloak DB_HOST=localhost DB_PORT=5432 DB_NAME=simple_forum DB_USER=... DB_PASS=... java -jar target/simple-forum-0.0.1-SNAPSHOT.jar 
```

```commandline
java -jar target/simple-forum-0.0.1-SNAPSHOT.jar \
--spring.datasource.url=jdbc:postgresql://localhost:5432/simple_forum \
--spring.datasource.username=simple-forum-app \
--spring.datasource.password=simple-forum-app \
--spring.security.oauth2.client.provider.keycloak.issuer-uri=http://localhost:8100/realms/SimpleForumKeycloak \
--spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8100/realms/SimpleForumKeycloak
```

### Running with Docker

This repository provides the necessary Dockerfile to build/run the service as a standalone Docker container, or part of Docker compose

```commandline
docker build -t simple-forum-core .

docker run -p 8080:8080 simple-forum-core 
```

### Perform DB Migration
We use flyway to perform migration

1. Specify the following environment variables:
   - DB_HOST
   - DB_PASS
   - DB_NAME
   - DB_PORT
   - DB_USER
2. Run `mvn flyway:migrate`