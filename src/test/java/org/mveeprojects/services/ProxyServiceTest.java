package org.mveeprojects.services;

import org.junit.jupiter.api.Test;
import org.mveeprojects.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ProxyServiceTest {

    private final String id = "123";
    private final String path = "/abc/" + id;
    private final String body = "{\n" +
            "\"customer_id\" : \"abc\",\n" +
            "\"name\" : \"Mark\",\n" +
            "\"contact_info\" : {\n" +
            "\t\"email\" : \"mark@mveeprojects.com\",\n" +
            "\t\"phone\" : \"1234\"\n" +
            "}\n" +
            "}";
    private final URI uri = new URI("http://wiremock:8080" + path);

    HttpClient testHttpClient = HttpClient.newHttpClient();

    @Autowired
    ProxyService testProxyService;

    public ProxyServiceTest() throws URISyntaxException {
    }

    @Test
    void generateTarget_generates_URI_correctly() throws URISyntaxException {
        URI actual = testProxyService.generateTarget("abc", id);
        URI expected = new URI("http://wiremock:8080/abc/123");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void generateTarget_empty_path_throws_URISyntaxException() {
        URISyntaxException uriSyntaxException = assertThrows(URISyntaxException.class, () ->
                testProxyService.generateTarget("", id));
        assertEquals("path and/or id must not be empty", uriSyntaxException.getReason());
    }

    @Test
    void generateTarget_empty_id_throws_URISyntaxException() {
        URISyntaxException uriSyntaxException = assertThrows(URISyntaxException.class, () ->
                testProxyService.generateTarget("abc", ""));
        assertEquals("path and/or id must not be empty", uriSyntaxException.getReason());
    }

    @Test
    void getResponse_result_generated_with_correct_path() throws URISyntaxException {
        HttpResult actual = testProxyService.getResponse(testHttpClient, uri, id);
        HttpResult expected = new TwoHundredResult(path, id, "{blah}");
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
    }

    @Test
    void checkHttpResponse_without_body_200() {
        HttpResult actual = testProxyService.checkHttpResponse(path, 200, id);
        HttpResult expected = new TwoHundredResult(path, id, "");
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
        assertThat(actual.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(actual.getCustomerId()).isEqualTo(expected.getCustomerId());
        assertThat(actual.getResponseBody()).isEqualTo(expected.getResponseBody());
    }

    @Test
    void checkHttpResponse_without_body_404() {
        HttpResult actual = testProxyService.checkHttpResponse(path, 404, id);
        HttpResult expected = new NotFoundErrorResult(path, id);
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
        assertThat(actual.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(actual.getCustomerId()).isEqualTo(expected.getCustomerId());
        assertThat(actual.getResponseBody()).isEqualTo(expected.getResponseBody());
    }

    @Test
    void checkHttpResponse_without_body_424() {
        HttpResult actual = testProxyService.checkHttpResponse(path, 424, id);
        HttpResult expected = new DownstreamServiceErrorResult(path, id);
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
        assertThat(actual.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(actual.getCustomerId()).isEqualTo(expected.getCustomerId());
        assertThat(actual.getResponseBody()).isEqualTo(expected.getResponseBody());
    }

    @Test
    void checkHttpResponse_without_body_5xx() {
        HttpResult actual = testProxyService.checkHttpResponse(path, 503, id);
        HttpResult expected = new UnknownErrorResult(path, id);
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
        assertThat(actual.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(actual.getCustomerId()).isEqualTo(expected.getCustomerId());
        assertThat(actual.getResponseBody()).isEqualTo(expected.getResponseBody());
    }

    @Test
    void checkHttpResponse_with_body_200() {
        HttpResult actual = testProxyService.checkHttpResponse(path, 200, id, body);
        HttpResult expected = new TwoHundredResult(path, id, body);
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
        assertThat(actual.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(actual.getCustomerId()).isEqualTo(expected.getCustomerId());
        assertThat(actual.getResponseBody()).isEqualTo(expected.getResponseBody());
    }

    @Test
    void checkHttpResponse_with_body_404() {
        HttpResult actual = testProxyService.checkHttpResponse(path, 404, id, body);
        HttpResult expected = new NotFoundErrorResult(path, id);
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
        assertThat(actual.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(actual.getCustomerId()).isEqualTo(expected.getCustomerId());
        assertThat(actual.getResponseBody()).isEqualTo(expected.getResponseBody());
    }

    @Test
    void checkHttpResponse_with_body_424() {
        HttpResult actual = testProxyService.checkHttpResponse(path, 424, id, body);
        HttpResult expected = new DownstreamServiceErrorResult(path, id);
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
        assertThat(actual.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(actual.getCustomerId()).isEqualTo(expected.getCustomerId());
        assertThat(actual.getResponseBody()).isEqualTo(expected.getResponseBody());
    }

    @Test
    void checkHttpResponse_with_body_5xx() {
        HttpResult actual = testProxyService.checkHttpResponse(path, 503, id, body);
        HttpResult expected = new UnknownErrorResult(path, id);
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
        assertThat(actual.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(actual.getCustomerId()).isEqualTo(expected.getCustomerId());
        assertThat(actual.getResponseBody()).isEqualTo(expected.getResponseBody());
    }

    @Test
    void buildRequest_generates_HttpRequest_correctly() {
        HttpRequest actual = testProxyService.buildRequest(uri);
        HttpRequest expected = HttpRequest.newBuilder()
                .uri(uri)
                .timeout(Duration.of(10, SECONDS.toChronoUnit()))
                .GET()
                .build();
        assertThat(actual.uri()).isEqualTo(expected.uri());
        assertThat(actual.method()).isEqualTo(expected.method());
        assertThat(actual.timeout()).isEqualTo(expected.timeout());
    }

    @Test
    void checkAndObfuscate_obfuscates_email_address_in_body() {
        HttpResult twoHundred = new TwoHundredResult(path, id, body);
        String actual = testProxyService.checkAndObfuscate(twoHundred).getResponseBody();
        assertThat(actual).contains("************");
        assertThat(actual).doesNotContain("mark@mveeprojects.com");
        System.out.println("actual:\n" + actual);
    }
}
