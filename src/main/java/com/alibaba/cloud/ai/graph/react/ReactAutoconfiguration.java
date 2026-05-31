package com.alibaba.cloud.ai.graph.react;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ReactAutoconfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}