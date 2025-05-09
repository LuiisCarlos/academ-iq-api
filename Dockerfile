FROM eclipse-temurin:latest
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
# $> docker build -t academ-iq-api:1.0.0-beta