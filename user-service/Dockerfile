FROM openjdk:17
ADD target/*.jar user-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar","/user-service.jar"]