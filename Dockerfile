FROM eclipse-temurin:21.0.2_13-jre-alpine
VOLUME /tmp
EXPOSE 8080
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
