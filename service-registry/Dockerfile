# Build stage
FROM maven:3.8.4-openjdk-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# Package stage
FROM adoptopenjdk:11-jre-hotspot
EXPOSE 8761
WORKDIR /app
COPY --from=build /app/target/service-registry.jar .
ENTRYPOINT ["java", "-jar", "service-registry.jar"]
