package org.mveeprojects.model;

public class ConnectionIssue extends HttpResult {
    public ConnectionIssue(String id) {
        super(500, id);
    }
}
