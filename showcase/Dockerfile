FROM amazoncorretto:21-alpine-jdk
COPY target/*.jar showcase-0.0.1.jar
COPY items showcase/items
ENTRYPOINT ["java","-jar","showcase-0.0.1.jar"]