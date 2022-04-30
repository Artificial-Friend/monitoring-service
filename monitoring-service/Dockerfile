#FROM maven:3-openjdk-17 AS build
#RUN mkdir -p /workspace
#WORKDIR /workspace
#COPY pom.xml /workspace
#COPY src /workspace/src
#RUN mvn -f pom.xml clean package

FROM openjdk:17
#COPY --from=build /workspace/target/*.jar monitoring-service.jar
ADD target/*.jar monitoring-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","/monitoring-service.jar"]