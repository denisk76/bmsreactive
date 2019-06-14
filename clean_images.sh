docker rm $(docker ps -aq)
docker rmi -f $(docker images -q)
docker volume prune -f
# docker rmi -f $(docker images -q --filter "dangling=true")
# docker rmi -f $(docker images -q --filter=reference='bmsgroup/*:*')