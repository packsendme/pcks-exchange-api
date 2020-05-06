
FROM openjdk:8-jdk-alpine
EXPOSE 9100
COPY /target/packsendme-exchange-api-0.0.1-SNAPSHOT.jar packsendme-exchange-api-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/packsendme-exchange-api-0.0.1-SNAPSHOT.jar"]