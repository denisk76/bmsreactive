if [[ -n $(docker images -q --filter "dangling=true") ]]
then
docker rmi -f $(docker images -q --filter "dangling=true")
fi
if [[ -n $(docker images -q --filter=reference='bmsgroup/*:*') ]]
then
docker rmi -f $(docker images -q --filter=reference='bmsgroup/*:*')
fi
mvn clean &&
mvn install &&
docker-compose build