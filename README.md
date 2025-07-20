# INTERSHOP

витрина интернет-магазина

## Настройка

Для запуска проекта нужно прописать в application.properties 
- `image.path`=(путь для хранения картинок в файловой системе).
- `items.path`=(путь с описанием подгружаемых товаров)

## Описание подгружаемых товаров

Предопределённое описание подгружаемых товаров хранится в папке `items`, в проекте. 

Внутри `items` есть набор папок, с числовыми названиями, внутри которых описан каждый товар.

В описании каждого товара есть файл `item.txt` в котором описан товар

- первая строка - название товара
- вторая строка - описание товара
- третья строка - цена товара

И возможно лежит вайл с картинкой товара(не обязательно).

Размер картинки желателен 300х300 а расширение должно быть `.jpg`, `.png`, `.jpeg`, `.gif` или `.webp`.

## Тестирование

Для успешного прохождения тестов нужно запустить keycloak
docker run -d -p 8082:8080 --name keycloak -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin -v ".\keycloak\realms\intershop.json:/opt/keycloak/data/import/intershop.json" quay.io/keycloak/keycloak:26.1.3 start-dev --import-realm
Где .\keycloak\realms\intershop.json заменить на абсолютный путь

## Запуск

### Запуск из среды разработки

#### Запуск сервиса платежей
Для запуска просто запускаем класс `IntershopApi` в модуле services
Описание сервиса будет доступно по адресу `http://localhost:8081/swagger-ui/index.html`

#### Запуск витрины
Для запуска, сначала подниммаем redis в контейнере
docker run --name redis-server -it --rm -p 6379:6379 redis:7.4.2-bookworm sh -c "redis-server & sleep 3 && redis-cli"

Потом поднимаем keycloak
docker run -d -p 8082:8080 --name keycloak -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin -v ".\keycloak\realms\intershop.json:/opt/keycloak/data/import/intershop.json" quay.io/keycloak/keycloak:26.1.3 start-dev --import-realm
Где .\keycloak\realms\intershop.json заменить на абсолютный путь

Потом просто запускаем класс `IntershopReactiveApplication` в модуле showcase
Приложение будет доступно по адресу `http://localhost:8080/`

### Запуск средствами Docker Compose

- Выполнить `package` в `maven`, для всего проекта чтобы собрались исполняемые файлы сервиса платежей, и витрины.
- Запустить Docker Compose `docker-compose up --build`
- После этого приложение будет доступно по адресу `http://localhost:8080/`, а описание сервиса по адресу `http://localhost:8081/swagger-ui/index.html`