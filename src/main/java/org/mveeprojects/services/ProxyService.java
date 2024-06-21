package org.mveeprojects.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.mveeprojects.config.DownstreamConfig;
import org.mveeprojects.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

@Service
public class ProxyService {

    @Autowired
    DownstreamConfig downstreamConfig;

    public HttpResult apiResponse(String path, String id) {
        HttpClient client = HttpClient.newHttpClient();
        HttpResult httpResult = getResponse(client, path, id);
        return checkAndObfuscate(httpResult);
    }

    protected HttpResult getResponse(HttpClient client, String path, String id) {

        URI uri;

        try {
            uri = generateTarget(path, id);
        } catch (URISyntaxException e) {
            return new InputIssue(path, id, e.getReason());
        }

        String body;
        int statusCode = 0;

        try {
            CompletableFuture<HttpResponse<String>> httpResponseCompletableFuture = client.sendAsync(buildRequest(uri), HttpResponse.BodyHandlers.ofString());
            body = httpResponseCompletableFuture.thenApply(HttpResponse::body).get();
            statusCode = httpResponseCompletableFuture.thenApply(HttpResponse::statusCode).get();
            return checkHttpResponse(path, statusCode, id, body);
        } catch (Exception e) {
            return checkHttpResponse(path, statusCode, id);
        }
    }

    private HttpResult checkHttpResponse(String path, int statusCode, String id) {
        return checkHttpResponse(path, statusCode, id, "");
    }

    private HttpResult checkHttpResponse(String path, int statusCode, String id, String body) {
        return switch (statusCode) {
            case 200 -> new TwoHundred(path, id, body);
            case 404 -> new NotFound(path, id);
            default -> new ConnectionIssue(path, id);
        };
    }

    protected URI generateTarget(String path, String id) throws URISyntaxException {

        if(path.isEmpty()) throw new URISyntaxException(path, "path must not be empty");
        else if(id.isEmpty()) throw new URISyntaxException(path, "id must not be empty");

        String prefix = downstreamConfig.getApiHostname(path) + ":" + downstreamConfig.getApiPort(path) + "/";

        try {
            return new URI(prefix + path + "/" + id);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    HttpRequest buildRequest(URI target) {
        return HttpRequest.newBuilder()
                .uri(target)
                .timeout(Duration.of(10, SECONDS.toChronoUnit()))
                .GET()
                .build();
    }

    private HttpResult checkAndObfuscate(HttpResult httpResult) {
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
