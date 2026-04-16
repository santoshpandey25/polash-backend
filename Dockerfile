# Step 1: Use a lightweight Java 21 runtime
FROM eclipse-temurin:21-jdk-alpine

# Step 2: Create a folder for the app inside the container
WORKDIR /app

# Step 3: Create the 'uploads' folder for selfies/liveness photos
RUN mkdir -p /app/uploads

# Step 4: Copy the JAR file from your computer to the container
# Ensure you run '.\mvnw.cmd clean package -DskipTests' first!
COPY target/backend-0.0.1-SNAPSHOT.jar POLASH.jar

# Step 5: Expose the port your app runs on
EXPOSE 8080

# Step 6: Command to run the app
ENTRYPOINT ["java", "-jar", "POLASH.jar"]
