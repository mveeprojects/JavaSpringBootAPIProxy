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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;

@Service
public class ProxyService {

    public JsonNode apiResponses(String name) {
        HttpClient client = HttpClient.newHttpClient();
        List<URI> targets = generateTargets(name);
        List<String> responses = callTargets(client, targets);
        return prettyJson(responses);
    }

    private List<String> callTargets(HttpClient client, List<URI> targets) {
        return targets.stream()
                .map(target ->
                        {
                            try {
                                return client.sendAsync(buildRequest(target), HttpResponse.BodyHandlers.ofString())
                                        .thenApply(HttpResponse::body).get();
                            } catch (InterruptedException | ExecutionException e) {
                                throw new RuntimeException(e);
                            }
                        }
                ).collect(Collectors.toList());
    }

    private List<URI> generateTargets(String name) {
        try {
            return Arrays.asList(
                    new URI("http://wiremock:8080/employee/" + name),
                    new URI("http://wiremock:8080/addresses/" + name));
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

    private JsonNode prettyJson(List<String> responses) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(responses.toString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
