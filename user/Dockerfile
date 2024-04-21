FROM eclipse-temurin:latest
COPY target/*.jar /user-service.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/user-service.jar"]
