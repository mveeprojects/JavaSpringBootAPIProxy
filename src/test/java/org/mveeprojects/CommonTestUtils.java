package org.mveeprojects;

import java.net.URI;

public interface CommonTestUtils {

    String id = "123";
    String path = "/abc/" + id;
    String body = "{\n" +
            "\"customer_id\" : \"abc\",\n" +
            "\"name\" : \"Mark\",\n" +
            "\"contact_info\" : {\n" +
            "\t\"email\" : \"mark@mveeprojects.com\",\n" +
            "\t\"phone\" : \"1234\"\n" +
            "}\n" +
            "}";
    String uriString = "http://wiremock:8080" + path;
}
