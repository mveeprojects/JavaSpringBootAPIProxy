package org.mveeprojects.services;

import org.junit.jupiter.api.Test;
import org.mveeprojects.CommonTestUtils;
import org.mveeprojects.model.*;
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

    @Test
    void getResponse_result_generated_with_correct_path() {
        HttpResult actual = testProxyService.getResponse(testHttpClient, uri, id);
        HttpResult expected = new TwoHundredResult(path, id, "{blah}");
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
    }
}
