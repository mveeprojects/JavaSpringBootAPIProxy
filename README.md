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

**Note:** To easily clean up the docker container and image, run `docker rm -f $(docker ps -aq) &&  docker rmi -f mveeprojects/java_sb_api_proxy`. 

Once running, navigate to http://localhost:8080/.

### Sources
* mveeprojects.wordpress
  * [Good Thymes with Spring Boot](https://mveeprojects.wordpress.com/2017/11/11/good-thymes-with-spring-boot/).
* Spring.io
  * [Spring Boot getting started guide](https://spring.io/guides/gs/spring-boot).
  * [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/).
  * [Spring Boot Docker](https://spring.io/guides/topicals/spring-boot-docker)
