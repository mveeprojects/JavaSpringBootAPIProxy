package org.mveeprojects.model;

public class TwoHundredResult extends HttpResult {
    public TwoHundredResult(String path, String id, String body) {
        super(path, 200, id, body);
    }
}
