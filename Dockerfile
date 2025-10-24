FROM maven:3.9.9-eclipse-temurin-21
WORKDIR /app

# Copy everything
COPY . .

# Build the app (skip tests, CI will run them separately)
RUN mvn clean package -DskipTests

# Run the Spring Boot JAR
CMD ["java", "-jar", "target/flashLearn-0.0.1-SNAPSHOT.jar"]
