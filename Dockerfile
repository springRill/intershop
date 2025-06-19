FROM amazoncorretto:21-alpine-jdk
COPY target/*.jar intershop-reactive-0.0.1.jar
COPY items /items
ENTRYPOINT ["java","-jar","intershop-reactive-0.0.1.jar"]