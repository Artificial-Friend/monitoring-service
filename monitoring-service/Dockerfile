FROM openjdk:17
ADD target/*.jar monitoring-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar","/monitoring-service.jar"]