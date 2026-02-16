# Weight Tracker API

A Spring Boot RESTful API for tracking user weight entries and goals, and for computing health metrics such as BMR, TDEE, and BMI. The service persists data in PostgreSQL, secures routes via a lightweight token verification interceptor, and is containerized for deployment.


## Overview
- Language: Java 21
- Frameworks/Libraries: Spring Boot 4.x (Web, Data JPA, Security, Actuator, WebFlux), Nimbus JOSE + JWT, Hibernate
- Database: PostgreSQL
- Build tool: Gradle (Gradle Wrapper included)
- Packaging: Fat JAR (Spring Boot)
- Container: Docker (GraalVM JDK 21 base image)
- Auth: Custom interceptor that verifies an `x-auth-token` via an external auth service

Main entry point: `src/main/java/com/shultzlab/weighttrackerapi/WeightTrackerApiApplication.java`

Exposed port: `8080` (default Spring Boot; see Dockerfile `EXPOSE 8080`)

Actuator: Included (e.g., `/actuator/health`) — endpoints depend on default Spring Boot actuator configuration.


## Requirements
- Java 21 JDK
- Gradle (optional; wrapper provided via `./gradlew`)
- PostgreSQL database
- Docker (optional, for container builds)
- (Optional) Google Cloud + Helm (for provided deployment scripts)


## Environment Variables
Spring configuration draws DB settings from environment variables (see `src/main/resources/application.properties`). Set the following for local/dev and production deployments:

- `SPRING_DATASOURCE_URL` — e.g., `jdbc:postgresql://localhost:5432/weighttracker`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

Additional relevant properties (already configured in `application.properties`):
- `spring.jpa.hibernate.ddl-auto=update` (schema auto-update)
- `spring.jpa.show-sql=true`
- `spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect`
- `spring.threads.virtual.enabled=true` (uses virtual threads)

Auth:
- Requests expect an `x-auth-token` (or `X-Auth-Token`) header. The interceptor calls `https://auth-api-go.shultzlab.com/verify` to validate the token.
- TODO: Make the auth verification base URL configurable via an environment variable (currently hardcoded in `AuthInterceptor`).


## Setup (Local Development)
1. Ensure PostgreSQL is running and create a database, e.g., `weighttracker`.
2. Export environment variables, for example:
   ```bash
   export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/weighttracker"
   export SPRING_DATASOURCE_USERNAME="postgres"
   export SPRING_DATASOURCE_PASSWORD="postgres"
   ```
3. Build the project:
   ```bash
   ./gradlew clean build
   ```
4. Run the application:
   ```bash
   ./gradlew bootRun
   ```
   or run the packaged JAR after building:
   ```bash
   java -jar build/libs/*.jar
   ```

The server will start on `http://localhost:8080` by default.


## Running with Docker
Build and run the Docker image:
```bash
# Build (uses Dockerfile in project root)
docker build -t weight-tracker-api:local .

# Run
docker run --rm -p 8080:8080 \
  -e SPRING_DATASOURCE_URL="jdbc:postgresql://host.docker.internal:5432/weighttracker" \
  -e SPRING_DATASOURCE_USERNAME="postgres" \
  -e SPRING_DATASOURCE_PASSWORD="postgres" \
  weight-tracker-api:local
```

Provided helper script to build and push to Google Artifact Registry:
```bash
./buildAndPushImage.sh
```
- Tags image as: `us-central1-docker.pkg.dev/shultzlab/docker-gcp/weight-tracker-api:latest`
- Requires `docker` logged in and permission to push to the specified registry/project.


## Helm Deployment
A helper script is included:
```bash
./helmDeploy.sh
```
It logs into the OCI registry and installs/upgrades the chart `spring-boot-http-deploy` (`version 0.1.1`) with values from `.helm/deploy/values.yaml`.

- TODO: Ensure `.helm/deploy/values.yaml` exists and contains environment-specific overrides (file not present in this repo listing).
- Prereqs: `gcloud` CLI auth, `helm` installed, and access to `us-central1-docker.pkg.dev/shultzlab/shultzlab-helm`.


## API Overview
All business endpoints expect an auth header: `x-auth-token: <token>`.

- Users (`/user`)
    - `POST /user` — Create user (body: `User`); requires `x-auth-token` matching username in payload.
    - `PUT /user/{id}` — Update user.
    - `DELETE /user/{id}` — Delete user.

- Weight Entries (`/entry`)
    - `GET /entry/username/{username}?time=<days|-1>` — List entries up to `time` days back (use `-1` for all).
    - `GET /entry/username/{username}/last` — Get most recent entry.
    - `POST /entry` — Create entry (body: `WeightEntryRequest`).
    - `PUT /entry/{id}` — Update entry.
    - `DELETE /entry/{id}` — Delete entry.

- Goals (`/goals`)
    - `GET /goals/{username}` — List goals for user.
    - `GET /goals/{username}/{id}` — Get a goal by id.
    - `GET /goals/{username}/goal/calorieBreakdown` — Compute calorie breakdown towards goal.
    - `POST /goals/{username}` — Create a goal.
    - `PUT /goals/{username}/{id}` — Update a goal.
    - `DELETE /goals/{username}/{id}` — Delete a goal.

- Stats (`/stats`)
    - `GET /stats/bmr/{username}` — Basal Metabolic Rate.
    - `GET /stats/tdee/{username}` — Total Daily Energy Expenditure.
    - `GET /stats/bmi/{username}` — Body Mass Index.
    - `GET /stats/all/{username}` — All stats summary.
    - `GET /stats/tdeeOptions/{username}` — TDEE options across activity levels.

- Actuator (examples; depends on Spring defaults)
    - `GET /actuator/health`

Note: Security filter permits all requests at the Spring Security layer (see `WebSecurityConfig`), but a global MVC interceptor (`AuthInterceptor`) enforces token validation for routes during request handling. Expect `TokenForbiddenException` when missing/invalid token.


## Scripts
- `buildAndPushImage.sh` — Builds the JAR, builds the Docker image, and pushes `:latest` to Google Artifact Registry.
- `helmDeploy.sh` — Logs into OCI registry and installs/upgrades the Helm release using a remote chart with local values.


## Tests
Run unit tests with:
```bash
./gradlew test
```
- JUnit Platform is enabled via Gradle.
- Sample test class: `src/test/java/com/shultzlab/weighttrackerapi/WeightTrackerApiApplicationTests.java`


## Project Structure
```
.
├── Dockerfile
├── build.gradle
├── buildAndPushImage.sh
├── gradle/
├── gradlew
├── gradlew.bat
├── helmDeploy.sh
├── settings.gradle
├── src
│   ├── main
│   │   ├── java/com/shultzlab/weighttrackerapi
│   │   │   ├── WeightTrackerApiApplication.java
│   │   │   ├── config/WebSecurityConfig.java
│   │   │   ├── controllers/{GoalController,StatsController,UserController,WeightEntryController}.java
│   │   │   ├── exceptions/{...}
│   │   │   ├── interceptors/{AuthInterceptor,Config}.java
│   │   │   ├── models/{...}
│   │   │   ├── repositories/{...}
│   │   │   └── services/{StatsService,TokenService,UserService}.java
│   │   └── resources/application.properties
│   └── test/java/com/shultzlab/weighttrackerapi/WeightTrackerApiApplicationTests.java
└── (README.md)
```


## Build
- Clean and build: `./gradlew clean build`
- Run locally: `./gradlew bootRun`
- Package JAR: `./gradlew bootJar`


## License
- TODO: Add license information (no license file detected).