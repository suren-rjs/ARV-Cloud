#
# Build stage
#
FROM maven:3.8.4-openjdk-11 AS build
COPY src /src
COPY pom.xml /
RUN mvn -f /pom.xml clean package

#
# Package stage
#
FROM adoptopenjdk:11-jre-hotspot
EXPOSE 8082
COPY --from=build target/auth-service.jar auth-service.jar
ENTRYPOINT ["java", "-jar", "/auth-service.jar"]
