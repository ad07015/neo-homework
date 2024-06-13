### Project information
- Author: Andrejs Daško
  - LinkedIn: **[Andrejs Daško](https://www.linkedin.com/in/andrejsdasko)**
  - Github profile: **[ad07015](https://github.com/ad07015)**
- Project Github repository: **[neo-homework](https://github.com/ad07015/neo-homework)**

### Running the application
- Run a PostgreSQL server in a docker container on a local machine on port 5432
  - docker run --name verywood-db -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 -d postgres
- Create a root database user
- Create a database by running the following SQL script:
  - CREATE DATABASE postgres
- Create a database schema by running the following SQL script:
  - CREATE SCHEMA IF NOT EXISTS neo
- Provide database username, password and URL in src/main/java/resources/application.yml, 
for example:
  - spring.datasource.url: jdbc:postgresql://localhost:5432/postgres
  - spring.datasource.username: postgres
  - spring.datasource.password: mysecretpassword
- Create a db schema 'neo' in your 'postgres' database by running the following SQL script:
  - CREATE SCHEMA IF NOT EXISTS schema_name
- Run the application by running the NeoPhoneNumberApiApplication.java class, Flyway will create the required database tables
- Proceed to using the application with Swagger or a dedicated REST API client of your choice, e.g. Postman

### Swagger
Swagger is a user interface that allows to manually run REST API calls without using a dedicated REST client like Postman.

Swagger UI is available here: **[swagger](http://localhost:8088/swagger-ui/index.html)**

### Requirements
**Functional requirements:**
- [x] Пользователь вводит номер телефона, приложение его валидирует и определяет страну. В случае удачного определения на экран выводится название страны, в противном случае сообщение об ошибке.
- [x] Приложение должно определять самую подходящую страну, например:
  - [x] Для номера «12423222931» ожидаемая страна “Bahamas”
  - [x] Для номера «11165384765» ожидаемые страны “United States, Canada”
  - [x] Для номера «71423423412» ожидаемая страна “Russia”
  - [x] Для номера «77112227231» ожидаемая страна “Kazakhstan”
- [x] Таблицу нужно загружать каждый раз при запуске приложения.
- [x] Все обращения к приложению выполняются через RESTful API с JSON в качестве формата данных.
- [x] Сервер должен запускаться на 8088 порту.

**Additional requirements:**
- [ ] Предусмотреть сборку и запуск приложения из командной строки. 
- [ ] Предусмотреть запуск тестов из командноий строки, а также формирование отчетов по результату их исполнения. 
- [ ] Внешниий вид интерфеийса неважен, достаточно простой и опрятноий вёрстки. 
- [x] Проект необходимо сопроводить README.md файлом с подробноий инструкциеий к запуску.
- [x] Проект должен быть публично доступным к просмотру в GitHub.