package com.alibaba.cloud.ai.graph.react.function;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.ai.alibaba.toolcalling.weather")
public class WeatherProperties {

    private boolean enabled = false;
    private String apiKey;
    private String baseUrl = "https://api.openweathermap.org/data/2.5/weather";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

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