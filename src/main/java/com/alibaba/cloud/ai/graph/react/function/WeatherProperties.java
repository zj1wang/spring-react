package com.alibaba.cloud.ai.graph.react.function;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.ai.weather")
public class WeatherProperties {

    private String apiKey;
    private String baseUrl = "https://api.openweathermap.org/data/2.5/weather";

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}