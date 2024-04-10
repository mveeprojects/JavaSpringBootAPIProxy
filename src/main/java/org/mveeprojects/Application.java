package org.mveeprojects;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        System.out.println("Starting up the application on localhost:80...");
        SpringApplication.run(Application.class, args);
        System.out.println("Application running.");
    }
}
