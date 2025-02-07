# syntax=docker/dockerfile:1

FROM eclipse-temurin:21-jdk-alpine 
COPY target/any-city-forecast-1.0-SNAPSHOT-shaded.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
