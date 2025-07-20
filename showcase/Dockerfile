FROM amazoncorretto:21-alpine-jdk

# Установка netcat
RUN apk add --no-cache netcat-openbsd

# Копируем скрипт ожидания
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Копируем JAR и данные
COPY target/showcase-0.0.1.jar showcase-0.0.1.jar
COPY items showcase/items

# Запуск: сначала ждем keycloak и redis, затем запускаем jar
ENTRYPOINT ["/wait-for-it.sh", "keycloak", "8080", "--", "/wait-for-it.sh", "redis", "6379", "--", "java", "-jar", "showcase-0.0.1.jar"]
