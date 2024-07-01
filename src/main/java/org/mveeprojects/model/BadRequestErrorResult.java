package org.mveeprojects.model;

public class BadRequestErrorResult extends HttpResult {
    public BadRequestErrorResult(String path, String id, String reason) {
        super(path,400, id, String.format("{\n\"error\":\"%1$s\"\n}", reason));
    }
}
