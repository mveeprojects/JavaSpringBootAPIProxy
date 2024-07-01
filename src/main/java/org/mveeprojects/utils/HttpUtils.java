package org.mveeprojects.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.mveeprojects.config.DownstreamConfig;
import org.mveeprojects.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.time.Duration;

import static java.util.concurrent.TimeUnit.SECONDS;

@Component
public class HttpUtils {

    @Autowired
    DownstreamConfig downstreamConfig;

    public URI generateTarget(String path, String id) throws URISyntaxException {
        String prefix = downstreamConfig.getApiHostname(path) + ":" + downstreamConfig.getApiPort(path) + "/";
        if (path.isEmpty() || id.isEmpty()) throw new URISyntaxException(path, "Path and/or id must not be empty");
        return new URI(prefix + path + "/" + id);
    }

    public HttpResult checkHttpResponse(String path, int statusCode, String id) {
        return checkHttpResponse(path, statusCode, id, "");
    }

    public HttpResult checkHttpResponse(String path, int statusCode, String id, String body) {
        return switch (statusCode) {
            case 200 -> new TwoHundredResult(path, id, body);
            case 404 -> new NotFoundErrorResult(path, id);
            case 424 -> new DownstreamServiceErrorResult(path, id);
            default -> new UnknownErrorResult(path, id);
        };
    }

    public HttpRequest buildRequest(URI target) {
        return HttpRequest.newBuilder()
                .uri(target)
                .timeout(Duration.of(10, SECONDS.toChronoUnit()))
                .GET()
                .build();
    }

    public HttpResult checkAndObfuscate(HttpResult httpResult) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode responseBody = mapper.readTree(httpResult.getResponseBody());

            if (responseBody.findValue("email") != null) {
                JsonNode contactInfoNode = responseBody.path("contact_info");
                ((ObjectNode) contactInfoNode).set("email", new TextNode("************"));
                String updatedJson = mapper.writeValueAsString(responseBody);
                responseBody = mapper.readTree(updatedJson);
            }

            httpResult.setResponseBody(responseBody.toPrettyString());
            return httpResult;
        } catch (Exception e) {
            return httpResult;
        }
    }
}
