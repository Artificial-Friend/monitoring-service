#FROM maven:3-openjdk-17 AS build
#RUN mkdir -p /workspace
#WORKDIR /workspace
#COPY pom.xml /workspace
#COPY src /workspace/src
#RUN mvn -f pom.xml clean package

FROM openjdk:17
#COPY --from=build /workspace/target/*.jar user-service.jar
ADD target/*.jar user-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar","/user-service.jar"]