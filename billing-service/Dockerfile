# ----------- Build Stage -----------
FROM gradle:8.7.0-jdk21 AS builder

WORKDIR /app

# Copy only files needed to cache dependencies
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Pre-fetch dependencies to cache them
RUN gradle build -x test --no-daemon || true

# Copy the full project
COPY . .

RUN gradle bootJar -x test --no-daemon

# ----------- Runtime Stage -----------
FROM eclipse-temurin:21-jdk AS runner

WORKDIR /app

# Copy jar from build stage
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 4001
EXPOSE 9001

ENTRYPOINT ["java", "-jar", "app.jar"]
