FROM openjdk:11

COPY build/libs/server-0.0.1-SNAPSHOT.jar Server.jar

ENTRYPOINT ["java","-jar","Server.jar"]

EXPOSE 8080