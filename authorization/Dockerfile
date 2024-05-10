FROM eclipse-temurin:latest
COPY target/*.jar /authorization-service.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "/authorization-service.jar"]
