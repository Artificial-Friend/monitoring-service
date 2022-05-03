FROM openjdk:17
ADD target/*.jar monitoring-result-service.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar","/monitoring-result-service.jar"]