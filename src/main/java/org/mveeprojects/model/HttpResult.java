package org.mveeprojects.model;

import java.util.Optional;

public abstract class HttpResult {

    int statusCode;

    String customerId;

    Optional<String> maybeResponseBody;

    public HttpResult(int statusCode, String customer_id) {
        this.statusCode = statusCode;
        this.customerId = customer_id;
    }

    public HttpResult(int statusCode, String customer_id, Optional<String> maybeResponseBody) {
        this.statusCode = statusCode;
        this.customerId = customer_id;
        this.maybeResponseBody = maybeResponseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getResponseBody() {
        return maybeResponseBody.orElse("{}");
    }
}
