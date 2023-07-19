# Build stage
FROM maven:3.8.4-openjdk-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# Package stage
FROM adoptopenjdk:11-jre-hotspot
EXPOSE 8081
WORKDIR /app
COPY --from=build /app/target/product-services.jar .
ENTRYPOINT ["java", "-jar", "product-services.jar"]
