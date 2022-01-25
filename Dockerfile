FROM openjdk:11

COPY ./build/libs/spring-authentication-0.0.1-SNAPSHOT.jar /app/app.jar
COPY ./build/resources/main/application.properties /app/application.properties

WORKDIR /app

CMD ["java","-Dspring.config.location=application.properties", "-jar", "app.jar"]