FROM openjdk:12-jdk-alpine
VOLUME /tmp
EXPOSE 8080
COPY target/*.jar app.jar
ENTRYPOINT ["java","-Dspring.config.location=./config/handler-service.yml","-Dlogging.config=./config/logback.xml","-jar","/app.jar"]