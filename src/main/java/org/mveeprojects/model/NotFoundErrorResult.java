package org.mveeprojects.model;

public class NotFoundErrorResult extends HttpResult {
    public NotFoundErrorResult(String path, String id) {
        super(path, 404, id);
    }
}
