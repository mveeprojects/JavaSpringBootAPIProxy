package org.mveeprojects.services;

import org.junit.jupiter.api.Test;
import org.mveeprojects.model.HttpResult;
import org.mveeprojects.model.InputIssue;
import org.mveeprojects.model.TwoHundred;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ProxyServiceTest {

    HttpClient testHttpClient = HttpClient.newHttpClient();

    @Autowired
    ProxyService testProxyService;

    @Test
    void generateTarget_happy() throws URISyntaxException {
        URI actualResult = testProxyService.generateTarget("abc", "123");
        URI expectedResult = new URI("http://wiremock:8080/abc/123");
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void generateTarget_unhappy_empty_path_throws_URISyntaxException() {
        URISyntaxException uriSyntaxException = assertThrows(URISyntaxException.class, () ->
                testProxyService.generateTarget("", "123"));
        assertEquals("path and/or id must not be empty", uriSyntaxException.getReason());
    }

    @Test
    void generateTarget_unhappy_empty_id_throws_URISyntaxException() {
        URISyntaxException uriSyntaxException = assertThrows(URISyntaxException.class, () ->
                testProxyService.generateTarget("abc", ""));
        assertEquals("path and/or id must not be empty", uriSyntaxException.getReason());    }

    @Test
    void getResponse_happy_result_generated_with_correct_path() throws URISyntaxException {
        HttpResult actualResult = testProxyService.getResponse(testHttpClient, new URI("http://wiremock:8080/abc/123"), "123");
        HttpResult expectedResult = new TwoHundred("/abc/123", "123", "{blah}");
        assertThat(actualResult.getPath()).isEqualTo(expectedResult.getPath());
    }
}
