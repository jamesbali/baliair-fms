# Use Eclipse Temurin base image for Java 21 (or whatever version you use)
FROM eclipse-temurin:21-jdk

# Set the working directory
WORKDIR /app

# Copy the JAR file (make sure it's built before)
COPY build/libs/baliair-fms-*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
