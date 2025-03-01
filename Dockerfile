# Dockerfile para el microservicio con Elasticsearch y Java 17
FROM openjdk:17-jdk-slim

# Copiar el archivo JAR desde el directorio 'target' al contenedor
COPY target/search-microservice-0.0.1-SNAPSHOT.jar search-microservice.jar

# Definir el comando de entrada para ejecutar el microservicio
ENTRYPOINT ["java", "-jar", "search-microservice.jar"]

# Exponer el puerto 8081 (o el puerto configurado en tu aplicación)
EXPOSE 8080
