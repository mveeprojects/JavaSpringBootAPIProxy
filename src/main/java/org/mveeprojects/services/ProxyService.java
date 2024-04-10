package org.mveeprojects.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.mveeprojects.model.ConnectionIssue;
import org.mveeprojects.model.HttpResult;
import org.mveeprojects.model.NotFound;
import org.mveeprojects.model.TwoHundred;
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

    public HttpResult apiResponse(String path, String id) {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = generateTarget(path, id);
        HttpResult httpResult = getResponse(client, uri, id);
        return checkAndObfuscate(httpResult);
    }

    private HttpResult getResponse(HttpClient client, URI uri, String id) {

        String body;
        int statusCode = 0;

        try {
            CompletableFuture<HttpResponse<String>> httpResponseCompletableFuture = client.sendAsync(buildRequest(uri), HttpResponse.BodyHandlers.ofString());
            body = httpResponseCompletableFuture.thenApply(HttpResponse::body).get();
            statusCode = httpResponseCompletableFuture.thenApply(HttpResponse::statusCode).get();
            return checkHttpResponse(statusCode, id, body);
        } catch (Exception e) {
            return checkHttpResponse(statusCode, id);
        }
    }

    private HttpResult checkHttpResponse(int statusCode, String id) {
        return checkHttpResponse(statusCode, id, "");
    }

    private HttpResult checkHttpResponse(int statusCode, String id, String body) {
        return switch (statusCode) {
            case 200 -> new TwoHundred(id, body);
            case 404 -> new NotFound(id);
            default -> new ConnectionIssue(id);
        };
    }

    private URI generateTarget(String path, String name) {

        String prefix = "http://wiremock:8080/";

        try {
            return new URI(prefix + path + "/" + name);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpRequest buildRequest(URI target) {
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
