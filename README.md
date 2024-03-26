# Java Spring Boot API Proxy

Stateless frontend application used to call a series of external REST APIs and present the result back to the user.

### Tech stack
* Java 21
* Spring Boot
* Thymeleaf
* Docker
* Wiremock

### Running the application

#### IntelliJ
* Click run on the [Application class](./src/main/java/org/mveeprojects/Application.java).

#### Gradle Plugin
* `./gradlew bootRun`.

**Docker**
* Application: `./gradlew build && docker build -t mveeprojects/java_sb_api_proxy . && docker run -d --name JavaSpringBootApiProxy -p 80:8080 mveeprojects/java_sb_api_proxy`.
* Wiremock: `docker run -d -p 8080:8080 --name wiremock -v ./wiremock/mappings:/home/wiremock/mappings wiremock/wiremock`
* Useful command to keep an eye on running docker containers `watch -n1 'docker ps -a --format "table {{.ID}}\t{{.Names}}\t{{.Ports}}\t{{.Status}}"'`
* To easily clean up the docker container and image, run `docker rm -f $(docker ps -aq) &&  docker rmi -f mveeprojects/java_sb_api_proxy`.

**Docker Compose**
* `./gradlew build && docker-compose down && docker rmi -f javespringbootapiproxy-javaspringbootapiproxy && docker-compose up -d`
  
Once running, the app will be available at localhost on port 80, wiremock will be available on port 8080.

### Endpoints

**API:**
* Successful API request - http://localhost/proxy/mark

**Wiremock:**
* View all mappings: http://localhost:8080/__admin/mappings
* Example API response 1: http://localhost:8080/employee/mark
* Example API response 2: http://localhost:8080/customer/sally

### Sources
* mveeprojects.wordpress
  * [Good Thymes with Spring Boot](https://mveeprojects.wordpress.com/2017/11/11/good-thymes-with-spring-boot/).
* Spring.io
  * [Spring Boot getting started guide](https://spring.io/guides/gs/spring-boot).
  * [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/).
  * [Spring Boot Docker](https://spring.io/guides/topicals/spring-boot-docker)
* Java HTTP Client
  * https://www.baeldung.com/java-9-http-client

### Next steps
* DONE ~~Run dockerised wiremock instance with mocked JSON response of an external REST API.~~
* DONE ~~Run the application and wiremock together in docker-compose.~~
* Add code to call the external API and print the JSON to the "/" endpoint.
* Create a simple UI using Thymeleaf.
* Render JSON of external API in "pretty print" in an iframe or similar on the UI.
* Add basic auth to external API call.
* Add PII to external API response JSON and obfuscate. 
