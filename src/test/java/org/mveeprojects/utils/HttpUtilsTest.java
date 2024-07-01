package org.mveeprojects.utils;

import org.junit.jupiter.api.Test;
import org.mveeprojects.CommonTestUtils;
import org.mveeprojects.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.time.Duration;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class HttpUtilsTest implements CommonTestUtils {

    public HttpUtilsTest() throws URISyntaxException {}

    final URI uri = new URI(uriString);

    @Autowired
    HttpUtils httpUtils;

    @Test
    void generateTarget_generates_URI_correctly() throws URISyntaxException {
        URI actual = httpUtils.generateTarget("abc", id);
        URI expected = new URI("http://wiremock:8080/abc/123");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void generateTarget_empty_path_throws_URISyntaxException() {
        URISyntaxException uriSyntaxException = assertThrows(URISyntaxException.class, () ->
                httpUtils.generateTarget("", id));
        assertEquals("Path and/or id must not be empty", uriSyntaxException.getReason());
    }

    @Test
    void generateTarget_empty_id_throws_URISyntaxException() {
        URISyntaxException uriSyntaxException = assertThrows(URISyntaxException.class, () ->
                httpUtils.generateTarget("abc", ""));
        assertEquals("Path and/or id must not be empty", uriSyntaxException.getReason());
    }

    @Test
    void checkHttpResponse_without_body_200() {
        HttpResult actual = httpUtils.checkHttpResponse(path, 200, id);
        HttpResult expected = new TwoHundredResult(path, id, "");
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
        assertThat(actual.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(actual.getCustomerId()).isEqualTo(expected.getCustomerId());
        assertThat(actual.getResponseBody()).isEqualTo(expected.getResponseBody());
    }

    @Test
    void checkHttpResponse_without_body_404() {
        HttpResult actual = httpUtils.checkHttpResponse(path, 404, id);
        HttpResult expected = new NotFoundErrorResult(path, id);
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
        assertThat(actual.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(actual.getCustomerId()).isEqualTo(expected.getCustomerId());
        assertThat(actual.getResponseBody()).isEqualTo(expected.getResponseBody());
    }

    @Test
    void checkHttpResponse_without_body_424() {
        HttpResult actual = httpUtils.checkHttpResponse(path, 424, id);
        HttpResult expected = new DownstreamServiceErrorResult(path, id);
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
        assertThat(actual.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(actual.getCustomerId()).isEqualTo(expected.getCustomerId());
        assertThat(actual.getResponseBody()).isEqualTo(expected.getResponseBody());
    }

    @Test
    void checkHttpResponse_without_body_5xx() {
        HttpResult actual = httpUtils.checkHttpResponse(path, 503, id);
        HttpResult expected = new UnknownErrorResult(path, id);
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
        assertThat(actual.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(actual.getCustomerId()).isEqualTo(expected.getCustomerId());
        assertThat(actual.getResponseBody()).isEqualTo(expected.getResponseBody());
    }

    @Test
    void checkHttpResponse_with_body_200() {
        HttpResult actual = httpUtils.checkHttpResponse(path, 200, id, body);
        HttpResult expected = new TwoHundredResult(path, id, body);
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
        assertThat(actual.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(actual.getCustomerId()).isEqualTo(expected.getCustomerId());
        assertThat(actual.getResponseBody()).isEqualTo(expected.getResponseBody());
    }

    @Test
    void checkHttpResponse_with_body_404() {
        HttpResult actual = httpUtils.checkHttpResponse(path, 404, id, body);
        HttpResult expected = new NotFoundErrorResult(path, id);
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
        assertThat(actual.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(actual.getCustomerId()).isEqualTo(expected.getCustomerId());
        assertThat(actual.getResponseBody()).isEqualTo(expected.getResponseBody());
    }

    @Test
    void checkHttpResponse_with_body_424() {
        HttpResult actual = httpUtils.checkHttpResponse(path, 424, id, body);
        HttpResult expected = new DownstreamServiceErrorResult(path, id);
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
        assertThat(actual.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(actual.getCustomerId()).isEqualTo(expected.getCustomerId());
        assertThat(actual.getResponseBody()).isEqualTo(expected.getResponseBody());
    }

    @Test
    void checkHttpResponse_with_body_5xx() {
        HttpResult actual = httpUtils.checkHttpResponse(path, 503, id, body);
        HttpResult expected = new UnknownErrorResult(path, id);
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
        assertThat(actual.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(actual.getCustomerId()).isEqualTo(expected.getCustomerId());
        assertThat(actual.getResponseBody()).isEqualTo(expected.getResponseBody());
    }

    @Test
    void buildRequest_generates_HttpRequest_correctly() {
        HttpRequest actual = httpUtils.buildRequest(uri);
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
        String actual = httpUtils.checkAndObfuscate(twoHundred).getResponseBody();
        assertThat(actual).contains("************");
        assertThat(actual).doesNotContain("mark@mveeprojects.com");
        System.out.println("actual:\n" + actual);
    }
}