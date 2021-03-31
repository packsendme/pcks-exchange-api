
FROM openjdk:8-jdk-alpine
EXPOSE 9200
COPY /target/pcks-exchange-api-0.0.1-SNAPSHOT.jar pcks-exchange-api-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/pcks-exchange-api-0.0.1-SNAPSHOT.jar"]