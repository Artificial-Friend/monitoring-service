FROM openjdk:17
ADD target/*.jar monitoring-service.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar","/monitoring-service.jar"]