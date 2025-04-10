# Use OpenJDK 21 base image
FROM openjdk:21-slim

# Install Maven 3.9.9
RUN apt-get update && apt-get install -y wget \
    && wget https://downloads.apache.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz \
    && tar -xzf apache-maven-3.9.9-bin.tar.gz -C /opt/ \
    && ln -s /opt/apache-maven-3.9.9/bin/mvn /usr/bin/mvn

# Set the working directory
WORKDIR /app

# Copy the Spring Boot application code
COPY . /app

# Build the application with Maven
RUN mvn clean install -DskipTests

# Expose the port your Spring Boot app will run on (e.g., 8080)
EXPOSE 8080

# Command to run your Spring Boot application
CMD ["java", "-jar", "target/player-service-0.0.1-SNAPSHOT.jar"]
