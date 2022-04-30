#FROM maven:3-openjdk-17 AS build
#RUN mkdir -p /workspace
#WORKDIR /workspace
#COPY pom.xml /workspace
#COPY src /workspace/src
#RUN mvn -f pom.xml clean package

FROM openjdk:17
#COPY --from=build /workspace/target/*.jar eureka-server.jar
ADD target/*.jar eureka-server.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar","/eureka-server.jar"]