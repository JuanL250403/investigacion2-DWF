FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 7070

ENTRYPOINT ["java", "-jar", "app.jar"]