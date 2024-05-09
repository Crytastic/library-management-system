FROM eclipse-temurin:latest
COPY target/*.jar /book-service.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "/book-service.jar"]
