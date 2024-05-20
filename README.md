### Project information
- Author: Andrejs Daško
  - LinkedIn: https://www.linkedin.com/in/andrejsdasko
  - Github profile: https://github.com/ad07015

- Project Github repository: https://github.com/ad07015/neo-homework

### Configuring the database
- Run a PostgreSQL server on your local machine on port 5432
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
- [ ] Extract data from Wikipedia API. Currently, data is populated by a Flyway script
- [ ] Map extracted JSON to a collection of CountryPhoneCode objects
  - [ ] Handle regular countries with one country code, e.g. Latvia - 371
  - [ ] Handle special cases for countries with multiple region codes, 
e.g. 'Isle of Man' - 44 (1624, 7524, 7624, 7924)
  - [ ] Test the mapper
- [ ] Load data into the PostgreSQL database
- [ ] Figure out how to find records where the provided phone number 
starts with the country code without using findAll()
- [ ] Implement a simple front page using Thymeleaf

**Quality of life improvements:**
- [ ] Create a docker-compose file to automatically pull and run the PostgreSQL container
on port 5432