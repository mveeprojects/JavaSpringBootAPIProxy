package org.mveeprojects.model;

public class InputIssue extends HttpResult {
    public InputIssue(String path, String id, String reason) {
        super(path,400, id, String.format("{\n\"error\":\"%1$s\"\n}", reason));
    }
}
