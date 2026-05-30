package com.alibaba.cloud.ai.graph.react.function;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@ConditionalOnProperty(name = "spring.ai.alibaba.toolcalling.weather.enabled", havingValue = "true")
public class WeatherAutoConfiguration {

    @Bean
    public WeatherService weatherService(WeatherProperties properties, RestClient restClient) {
        return new WeatherService(properties, restClient);
    }

    @Bean
    public WeatherUtils weatherUtils(WeatherService weatherService) {
        return new WeatherUtils(weatherService);
    }
}