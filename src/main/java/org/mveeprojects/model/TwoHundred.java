package org.mveeprojects.model;

import java.util.Optional;

public class TwoHundred extends HttpResult {
    public TwoHundred(String id, String body) {
        super(200, id, Optional.of(body));
    }
}
