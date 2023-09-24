# entitysort

## Run application
```
H2_SERVER_PORT=18085 SERVER_PORT=18084 ./gradlew clean bootRun
```
The default ports are:
Application: 8081
DB Server: 9092

### Connect to DB Server
URL: jdbc:h2:tcp://localhost:18084/mem:db1
User: sa
Password: <empty>

## Links
- Home page: http://localhost:18084/
- GraphViz Colors: https://graphviz.org/doc/info/colors.html
- DB Diagram IO: https://dbdiagram.io/d