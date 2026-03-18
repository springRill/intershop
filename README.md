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

И возможно лежит файл с картинкой товара(не обязательно).

Размер картинки желателен 300х300 а расширение должно быть `.jpg`, `.png`, `.jpeg`, `.gif` или `.webp`.

## Тестирование

Для успешного прохождения тестов нужно запустить keycloak keycloak/docker-compose.yml  
или через консоль `docker run -d -p 8082:8080 --name keycloak -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin -v ".\keycloak\realms\intershop.json:/opt/keycloak/data/import/intershop.json" quay.io/keycloak/keycloak:26.1.3 start-dev --import-realm`, 
где .\keycloak\realms\intershop.json заменить на абсолютный путь

## Запуск

### Запуск из среды разработки

#### Генерация сервисов
Выполнить `compile` в `maven` для генерации сервисов

#### Запуск сервиса платежей
Для запуска просто запускаем класс `IntershopApi` в модуле services
Описание сервиса будет доступно по адресу http://localhost:8081/swagger-ui/index.html

#### Запуск витрины
Для запуска, поднимаем redis в контейнере через `redis/docker-compose.yml`  
или через консоль `docker run --name redis-server -it --rm -p 6379:6379 redis:7.4.2-bookworm sh -c "redis-server & sleep 3 && redis-cli"`

Поднимаем keycloak в контейнере через `keycloak/docker-compose.yml`
или через консоль `docker run -d -p 8082:8080 --name keycloak -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin -v ".\keycloak\realms\intershop.json:/opt/keycloak/data/import/intershop.json" quay.io/keycloak/keycloak:26.1.3 start-dev --import-realm`,
где `.\keycloak\realms\intershop.json` заменить на абсолютный путь.  
Keycloak будет досьупен по адресу http://localhost:8082/, login:`admin`  password:`admin`

Запускаем класс `IntershopReactiveApplication` в модуле showcase
Приложение будет доступно по адресу http://localhost:8080/

### Запуск средствами Docker Compose

- Файл `showcase/wait-for-it.sh` должен быть с разделителем строки `LF` 
- Выполнить `package` в `maven`(с выключенными тестами, или работающим keycloak), для всего проекта чтобы собрались исполняемые файлы сервиса платежей, и витрины.
- Запустить приложение через `docker-compose.yml`, или в консоли `docker-compose up --build`
- После этого приложение будет доступно по адресу http://localhost:8080/, а описание сервиса по адресу `http://localhost:8081/swagger-ui/index.html`

### Пользователи приложения(login/password)

`user1`/`password1`  
`user2`/`password2`  
`11`/`11`  
`22`/`22`  
