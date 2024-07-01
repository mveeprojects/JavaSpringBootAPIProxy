package org.mveeprojects.services;

import org.junit.jupiter.api.Test;
import org.mveeprojects.CommonTestUtils;
import org.mveeprojects.model.*;
import org.mveeprojects.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProxyServiceTest implements CommonTestUtils {

    public ProxyServiceTest() throws URISyntaxException {}

    final URI uri = new URI(uriString);

    HttpClient testHttpClient = HttpClient.newHttpClient();

    @Autowired
    ProxyService testProxyService;

    @Autowired
    HttpUtils httpUtils;

    @Test
    void callDownstreamAPI_error_if_given_bad_input_for_uri_construction_junk() {
        HttpResult actual = testProxyService.callDownstreamAPI("blah blah", id);
        HttpResult expected = new BadRequestErrorResult("blah blah", id, "Illegal character in path");
        assertThat(actual.getResponseBody()).isEqualTo(expected.getResponseBody());
    }

    @Test
    void callDownstreamAPI_error_if_given_bad_input_for_uri_construction_null() {
        HttpResult actual = testProxyService.callDownstreamAPI(null, id);
        HttpResult expected = new BadRequestErrorResult(null, id, "Path cannot contain null elements");
        assertThat(actual.getResponseBody()).isEqualTo(expected.getResponseBody());
    }

    @Test
    void callDownstreamAPI_error_if_given_bad_input_for_uri_construction_empty_string() {
        HttpResult actual = testProxyService.callDownstreamAPI("", id);
        HttpResult expected = new BadRequestErrorResult("", id, "Path and/or id must not be empty");
        assertThat(actual.getResponseBody()).isEqualTo(expected.getResponseBody());
    }

    @Test
    void getResponse_result_generated_with_correct_path() {
        HttpResult actual = testProxyService.getResponse(testHttpClient, uri, id);
        HttpResult expected = new TwoHundredResult(path, id, "{blah}");
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
    }
}
