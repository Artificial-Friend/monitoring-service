FROM openjdk:17
ADD target/*.jar gateway-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","/gateway-service.jar"]