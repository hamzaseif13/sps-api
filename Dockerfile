# Stage 1: Build the JAR file
FROM maven:3.9.2-eclipse-temurin-17-alpine as builder

WORKDIR /app

# Copy only the necessary build files

COPY pom.xml .
COPY src src

# Build the application JAR file
RUN mvn clean package

# Stage 2: Create the final image
FROM openjdk:17

WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/SPS-*.jar app.jar

# Expose the application port
EXPOSE 8080

# Set the entrypoint command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
