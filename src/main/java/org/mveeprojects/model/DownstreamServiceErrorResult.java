package org.mveeprojects.model;

public class DownstreamServiceErrorResult extends HttpResult {
    public DownstreamServiceErrorResult(String path, String id) {
        super(path,424, id);
    }
}
