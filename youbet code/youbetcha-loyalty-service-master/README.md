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

http://localhost:17500/registration-service/swagger-ui.html
http://localhost:17500/registration-service/health
http://localhost:17400/registration-service/h2-console

## Increment release number

```bash
./gradlew releaseVersion
```

### Jenkins pipeline

Spin up jenkins via docker-compose-devops.yml
Create pipeline project
Import file via SCM in settings
