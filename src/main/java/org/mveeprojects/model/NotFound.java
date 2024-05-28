package org.mveeprojects.model;

public class NotFound extends HttpResult {
    public NotFound(String path, String id) {
        super(path, 404, id);
    }
}
