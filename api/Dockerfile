FROM eclipse-temurin:17.0.12_7-jdk-jammy

RUN mkdir -p /app \
    && addgroup api && adduser --ingroup api --disabled-password api \
    && chown -R api:api /app \
    && chmod -R u=rwx,g=rwx /app

WORKDIR /app

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app/api.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "/app/api.jar" ]