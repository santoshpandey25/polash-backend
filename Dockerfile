# STAGE 1: Build the JAR inside Railway's cloud
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only the files needed for building
COPY pom.xml .
COPY src ./src

# Run the maven build to create the JAR file
RUN mvn clean package -DskipTests

# STAGE 2: Create the final lightweight image to run the app
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Create the uploads folder
RUN mkdir -p /app/uploads

# Copy the JAR from the 'build' stage above
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar POLASH.jar

EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "POLASH.jar"]
