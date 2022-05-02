FROM openjdk:17
ADD target/*.jar monitoring-result-service.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar","/monitoring-result-service.jar"]