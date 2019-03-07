FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8080
COPY target/*.jar app.jar
ENTRYPOINT ["java","-Dspring.config.location=./config/zipkin.yml","-jar","/app.jar"]