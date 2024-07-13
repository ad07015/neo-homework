### Project information
- Author: Andrejs Daško
  - LinkedIn: **[Andrejs Daško](https://www.linkedin.com/in/andrejsdasko)**
  - Github profile: **[ad07015](https://github.com/ad07015)**
- Project Github repository: **[neo-homework](https://github.com/ad07015/neo-homework)**

### Running the application
- Run the application by running the NeoPhoneNumberApiApplication.java class, docker will spin up the database, and spring will create the necessary tables
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