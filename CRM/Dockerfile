FROM amazoncorretto:21-alpine
LABEL description="Образ для микросервиса 'CRM'"

ARG JAR_FILE=target/CRM-0.0.1-SNAPSHOT.jar
COPY $JAR_FILE CRM.jar
RUN apk --no-cache add curl

ENTRYPOINT ["java", "-jar", "-Dspring.output.ansi.enabled=ALWAYS", "/CRM.jar"]
