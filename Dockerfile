# Dockerfile para el microservicio con Elasticsearch y Java 17
FROM openjdk:17-jdk-slim
COPY target/my-service-elasticsearch.jar my-service-elasticsearch.jar
ENTRYPOINT ["java", "-jar", "my-service-elasticsearch.jar"]
EXPOSE 8082
