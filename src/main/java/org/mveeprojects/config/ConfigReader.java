package org.mveeprojects.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigReader {

    @Bean
    public DownstreamConfig downstreamConfig(
            @Value("${historyapi.host}") String historyapihost,
            @Value("${historyapi.port}") int historyapiport,
            @Value("${infoapi.host}") String infoapihost,
            @Value("${infoapi.port}") int infoapiport) {
        return new DownstreamConfig(historyapihost, historyapiport, infoapihost, infoapiport);
    }
}
