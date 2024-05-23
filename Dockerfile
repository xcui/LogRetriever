# First stage: build the application
FROM openjdk:17-jdk-slim AS build

# Set the working directory
WORKDIR /app

# Copy the Maven Wrapper and make it executable
COPY .mvn/ .mvn
COPY mvnw mvnw
COPY mvnw.cmd mvnw.cmd
COPY pom.xml .

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy the source code and build the application
COPY src ./src
RUN ./mvnw clean package

# Second stage: run the application
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built jar file from the first stage
COPY --from=build /app/target/logretriever-0.0.1-SNAPSHOT.jar /app/logretriever.jar

# Expose port 8080
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "logretriever.jar"]
