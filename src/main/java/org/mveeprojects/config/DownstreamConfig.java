package org.mveeprojects.config;

public class DownstreamConfig {

    String historyapihostname;
    int historyapiport;
    String infoapihostname;
    int infoapiport;

    public DownstreamConfig(String historyapihostname, int historyapiport, String infoapihostname, int infoapiport) {
        this.historyapihostname = historyapihostname;
        this.historyapiport = historyapiport;
        this.infoapihostname = infoapihostname;
        this.infoapiport = infoapiport;
    }

    public String getApiHostname(String path) {
        if (path.contains("info")) {
            return infoapihostname;
        } else {
            return historyapihostname;
        }
    }

    public int getApiPort(String path) {
        if (path.contains("info")) {
            return infoapiport;
        } else {
            return historyapiport;
        }
    }
}
