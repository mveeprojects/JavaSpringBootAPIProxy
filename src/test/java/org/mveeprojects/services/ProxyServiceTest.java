package org.mveeprojects.services;

import org.junit.jupiter.api.Test;
import org.mveeprojects.config.DownstreamConfig;
import org.mveeprojects.model.HttpResult;
import org.mveeprojects.model.InputIssue;
import org.mveeprojects.model.TwoHundred;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;

import static org.assertj.core.api.Assertions.assertThat;

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
    void getResponse_happy_result_generated_with_correct_path() {
        HttpResult actualResult = testProxyService.getResponse(testHttpClient, "abc", "123");
        HttpResult expectedResult = new TwoHundred("abc", "123", "{blah}");
        assertThat(actualResult.getPath()).isEqualTo(expectedResult.getPath());
    }

    @Test
    void getResponse_unhappy_empty_path() {
        HttpResult actualResult = testProxyService.getResponse(testHttpClient, "", "123");
        HttpResult expectedResult = new InputIssue("", "123", "path must not be empty");
        assertThat(actualResult.getPath()).isEqualTo(expectedResult.getPath());
        assertThat(actualResult.getCustomerId()).isEqualTo(expectedResult.getCustomerId());
        assertThat(actualResult.getResponseBody()).contains(expectedResult.getResponseBody());
    }

    @Test
    void getResponse_unhappy_empty_id() {
        HttpResult actualResult = testProxyService.getResponse(testHttpClient, "abc", "");
        HttpResult expectedResult = new InputIssue("abc", "", "id must not be empty");
        assertThat(actualResult.getPath()).isEqualTo(expectedResult.getPath());
        assertThat(actualResult.getCustomerId()).isEqualTo(expectedResult.getCustomerId());
        assertThat(actualResult.getResponseBody()).contains(expectedResult.getResponseBody());
    }
}
