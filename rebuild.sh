mvn clean &&
mvn package -Dmaven.test.skip=true &&
docker-compose build &&
mvn failsafe:integration-test &&
mvn failsafe:verify