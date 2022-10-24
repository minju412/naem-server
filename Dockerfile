FROM openjdk:17-ea-11-jdk-slim

VOLUME /tmp

ARG JAR_FILE=build/libs/server-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} Server.jar

#COPY build/libs/server-0.0.1-SNAPSHOT.jar Server.jar

ENTRYPOINT ["java","-jar","/Server.jar"]

EXPOSE 8080
