# Stage 1: Build the application
FROM maven:3.8.1-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src src
RUN mvn clean package -DskipTests

# Stage 2: Start the application
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/quotes-0.0.1-SNAPSHOT.jar quotes-0.0.1-SNAPSHOT.jar
EXPOSE 8080

COPY wait-for-it.sh /usr/local/bin/wait-for-it.sh
RUN apk add --no-cache bash && chmod +x /usr/local/bin/wait-for-it.sh

# Run the producer script
CMD ["wait-for-it.sh", "kafka:9092", "--", "java", "-jar", "quotes-0.0.1-SNAPSHOT.jar"]
