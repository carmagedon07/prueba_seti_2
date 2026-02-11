# syntax=docker/dockerfile:1

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY applications/app-service/build/libs/test_prueba_setic_2.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
