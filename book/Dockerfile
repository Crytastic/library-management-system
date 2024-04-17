FROM eclipse-temurin:latest
COPY target/*.jar /book-service.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "/book-service.jar"]
