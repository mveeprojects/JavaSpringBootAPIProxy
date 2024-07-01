package org.mveeprojects.services;

import org.mveeprojects.model.*;
import org.mveeprojects.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@Service
public class ProxyService {

    @Autowired
    HttpUtils httpUtils;

    public HttpResult apiResponse(String path, String id) {
        HttpClient client = HttpClient.newHttpClient();

        URI uri;

        try {
            uri = httpUtils.generateTarget(path, id);
        } catch (URISyntaxException e) {
            return new BadRequestErrorResult(path, id, e.getReason());
        }

        HttpResult httpResult = getResponse(client, uri, id);
        return httpUtils.checkAndObfuscate(httpResult);
    }

    protected HttpResult getResponse(HttpClient client, URI uri, String id) {

        String body;
        int statusCode = 0;

        try {
            CompletableFuture<HttpResponse<String>> httpResponseCompletableFuture = client.sendAsync(httpUtils.buildRequest(uri), HttpResponse.BodyHandlers.ofString());
            body = httpResponseCompletableFuture.thenApply(HttpResponse::body).get();
            statusCode = httpResponseCompletableFuture.thenApply(HttpResponse::statusCode).get();
            return httpUtils.checkHttpResponse(uri.getPath(), statusCode, id, body);
        } catch (Exception e) {
            return httpUtils.checkHttpResponse(uri.getPath(), statusCode, id);
        }
    }
}
