package org.mveeprojects.model;

public class ConnectionIssue extends HttpResult {
    public ConnectionIssue(String path, String id) {
        super(path,500, id);
    }
}
