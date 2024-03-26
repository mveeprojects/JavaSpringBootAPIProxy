package org.mveeprojects.services;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.stream.Stream;

import static java.util.concurrent.TimeUnit.SECONDS;

@Service
public class ProxyService {

    public List<String> apiResponses(String name) throws URISyntaxException {

        HttpClient client = HttpClient.newHttpClient();

        List<URI> targets = Arrays.asList(
                new URI("http://wiremock:8080/employee/" + name),
                new URI("http://wiremock:8080/addresses/" + name));

        List<String> responses = targets.stream()
                .map(target ->
                        {
                            try {
                                return client.sendAsync(buildRequest(target), HttpResponse.BodyHandlers.ofString())
                                        .thenApply(HttpResponse::body).get();
                            } catch (InterruptedException | ExecutionException e) {
                                System.out.println();
                                throw new RuntimeException(e);
                            }
                        }
                ).collect(Collectors.toList());
        return prettyJson(responses).toList();
    }

    private HttpRequest buildRequest(URI target) {
        return HttpRequest.newBuilder()
                .uri(target)
                .timeout(Duration.of(10, SECONDS.toChronoUnit()))
                .GET()
                .build();
    }

    private Stream<String> prettyJson(List<String> responses) {
        ObjectMapper mapper = new ObjectMapper();
        return responses.stream().map(x -> {
            try {
                return mapper.readTree(x).toPrettyString();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
