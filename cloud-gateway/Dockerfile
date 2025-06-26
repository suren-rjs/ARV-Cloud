# Build stage
FROM maven:3.8.4-openjdk-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# Package stage
FROM adoptopenjdk:11-jre-hotspot
EXPOSE 8090
WORKDIR /app
COPY --from=build /app/target/cloud-gateway.jar .
ENTRYPOINT ["java", "-jar", "cloud-gateway.jar"]
