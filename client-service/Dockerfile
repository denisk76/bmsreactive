FROM openjdk:12-jdk-alpine
VOLUME /tmp
ADD http://192.168.4.174:8081/artifactory/libs-release-local/wait /wait
RUN chmod +x /wait
EXPOSE 8080
COPY target/*.jar app.jar
CMD ./wait && java -Dspring.config.location=./config/client-service.yml -Dlogging.config=./config/logback.xml -jar /app.jar