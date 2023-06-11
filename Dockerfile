FROM openjdk:17

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
COPY target/SPS-*.jar app.jar

# Expose the application port
EXPOSE 80
EXPOSE 8080
ENV spring.profiles.active=prod

# Set the entrypoint command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]