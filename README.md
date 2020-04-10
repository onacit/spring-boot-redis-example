# spring-boot-redis-example

An example of using Redis with Spring Boot

## Redis with Docker

### Run Redis

You can run a Redis instance using `docker.run.sh` script

```shell script
$ sh ./docker.run.sh
```

which runs following command.

```shell script
docker run --rm -p 6379:6379 --name some-redis -d redis
```

### Connect to Redis

You can connect to the Redis using `docker.cli.sh` script.

```shell script
$ sh ./docker.cli.sh
```

which runs following command.

```shell script
docker exec -it some-redis redis-cli
```

### Stop Redis

You can stop the Redis using `docker.stop.sh` script.

```shell script
$ sh ./docker.stop.sh
```

which runs following command.

```shell script
docker stop some-redis
```

## The Spring Boot Application

Run the Application while your Redis is running.

```shell script
$ mvn spring-boot:run
```
