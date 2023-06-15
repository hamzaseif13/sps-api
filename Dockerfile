# Stage 1: Build the JAR file
FROM openjdk:17 as builder

WORKDIR /app

# Copy only the necessary build files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Build the application JAR file
RUN ./mvnw clean package

# Stage 2: Create the final image
FROM openjdk:17

WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/SPS-*.jar app.jar

# Expose the application port
EXPOSE 8080

# Set the entrypoint command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
