package com.alibaba.cloud.ai.graph.react.function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeatherAutoConfiguration {

    @Bean
    public WeatherService weatherService() {
        return new WeatherService();
    }

    @Bean
    public WeatherUtils weatherUtils(WeatherService weatherService) {
        return new WeatherUtils(weatherService);
    }
}