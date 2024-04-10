package org.mveeprojects.model;

public abstract class HttpResult {

    int statusCode;

    String customerId;

    String responseBody = "{}";

    public HttpResult(int statusCode, String customer_id) {
        this.statusCode = statusCode;
        this.customerId = customer_id;
    }

    public HttpResult(int statusCode, String customer_id, String responseBody) {
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
}
