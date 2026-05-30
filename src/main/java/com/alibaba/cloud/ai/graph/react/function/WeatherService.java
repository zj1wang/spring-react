package com.alibaba.cloud.ai.graph.react.function;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestClient;

public class WeatherService {

    private final WeatherProperties properties;
    private final RestClient restClient;

    public WeatherService(WeatherProperties properties, @Qualifier("reactAgentRestClient") RestClient restClient) {
        this.properties = properties;
        this.restClient = restClient;
    }

    public String getWeather(String city) {
        String url = properties.getBaseUrl() + "?q=" + city + "&appid=" + properties.getApiKey() + "&units=metric&lang=zh_cn";
        
        try {
            return restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);
        } catch (Exception e) {
            return "{\"error\": \"获取天气失败: " + e.getMessage() + "\"}";
        }
    }
}