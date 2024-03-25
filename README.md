# Java Spring Boot API Proxy

Stateless frontend application used to call a series of external REST APIs and present the result back to the user.

### Tech stack
* Java 21
* Spring Boot
* Thymeleaf
* Docker
* Wiremock

### Running the application

**IntelliJ** - click run on the [Application class](./src/main/java/org/mveeprojects/Application.java).

**Gradle Plugin** - `./gradlew bootRun`.

**Docker** - `./gradlew build && docker build -t mveeprojects/java_sb_api_proxy . && docker run -d --name JavaSpringBootApiProxy -p 8080:8080 mveeprojects/java_sb_api_proxy`.

**Note:**
* Useful command to keep an eye on running docker containers `watch -n1 'docker ps -a --format "table {{.ID}}\t{{.Names}}\t{{.Ports}}\t{{.Status}}"'`
* To easily clean up the docker container and image, run `docker rm -f $(docker ps -aq) &&  docker rmi -f mveeprojects/java_sb_api_proxy`. 

Once running, navigate to http://localhost:8080/.

### Sources
* mveeprojects.wordpress
  * [Good Thymes with Spring Boot](https://mveeprojects.wordpress.com/2017/11/11/good-thymes-with-spring-boot/).
* Spring.io
  * [Spring Boot getting started guide](https://spring.io/guides/gs/spring-boot).
  * [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/).
  * [Spring Boot Docker](https://spring.io/guides/topicals/spring-boot-docker)

### Next steps
* Run dockerised wiremock instance with mocked JSON response of an external REST API.
* Add code to call the external API and print the JSON to the "/" endpoint.
* Run the application and wiremock together in docker-compose.
* Create a simple UI using Thymeleaf.
* Render JSON of external API in "pretty print" in an iframe or similar on the UI.
* Add basic auth to external API call.
* Add PII to external API response JSON and obfuscate. 
