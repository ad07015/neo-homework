### Project information
- Author: Andrejs Daško
  - LinkedIn: **[Andrejs Daško](https://www.linkedin.com/in/andrejsdasko)**
  - Github profile: **[ad07015](https://github.com/ad07015)**
- Project Github repository: **[neo-homework](https://github.com/ad07015/neo-homework)**

### Configuring the database
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

Swagger for this application is available here: **[swagger](http://localhost:8088/swagger-ui/index.html)**

### TODO
**Functional requirements:**
- [ ] Handle special cases for countries with multiple region codes, 
e.g. 'Isle of Man' - 44 (1624, 7524, 7624, 7924)
- [ ] Implement a simple front page

**Quality of life improvements:**
- [ ] Create a docker-compose file to automatically pull and run the PostgreSQL container
on port 5432