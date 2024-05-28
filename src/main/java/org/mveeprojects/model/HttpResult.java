package org.mveeprojects.model;

public abstract class HttpResult {

    String path;

    int statusCode;

    String customerId;

    String responseBody = "{}";

    public HttpResult(String path, int statusCode, String customer_id) {
        this.path = path;
        this.statusCode = statusCode;
        this.customerId = customer_id;
    }

    public HttpResult(String path, int statusCode, String customer_id, String responseBody) {
        this.path = path;
        this.statusCode = statusCode;
        this.customerId = customer_id;
        this.responseBody = responseBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getPath() {
        return path;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getCustomerId() {
        return customerId;
    }
}
