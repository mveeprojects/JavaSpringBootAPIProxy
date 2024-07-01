package org.mveeprojects.model;

public class UnknownErrorResult extends HttpResult {
    public UnknownErrorResult(String path, String id) {
        super(path,500, id);
    }
}
