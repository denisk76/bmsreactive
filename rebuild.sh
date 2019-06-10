mvn clean &&
mvn install -Dmaven.test.skip=true &&
docker-compose build
