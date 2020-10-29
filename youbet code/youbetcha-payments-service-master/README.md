## Building

```bash
./gradlew build
./gradlew bootJar
```

## Build docker file

```bash
docker-compose build
then
docker-compose up
```
## Useful Urls

http://localhost:17700/payments-service/swagger-ui.html
http://localhost:17700/payments-service/health
http://localhost:17700/payments-service/h2-console

## Increment release number

```bash
./gradlew releaseVersion
```