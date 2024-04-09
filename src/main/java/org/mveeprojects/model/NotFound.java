package org.mveeprojects.model;

public class NotFound extends HttpResult {
    public NotFound(String id) {
        super(404, id);
    }
}
