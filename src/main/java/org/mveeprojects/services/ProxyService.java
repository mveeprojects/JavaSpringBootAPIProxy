package org.mveeprojects.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.SECONDS;

@Service
public class ProxyService {

    public JsonNode apiResponse(String path, String name) {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = generateTarget(path, name);
        String jsonResponse = getResponse(client, uri);
        return prettyJson(jsonResponse);
    }

    private String getResponse(HttpClient client, URI uri) {
        try {
            return client.sendAsync(buildRequest(uri), HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
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

    private JsonNode prettyJson(String jsonResponse) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(jsonResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
