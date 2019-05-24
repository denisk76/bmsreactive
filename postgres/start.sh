docker run --name questionmarks-psql \
    -p 5432:5432 \
    -e POSTGRES_DB=questionmarks \
    -e POSTGRES_PASSWORD=mysecretpassword \
    -v `pwd`/init:/docker-entrypoint-initdb.d \
    -d postgres