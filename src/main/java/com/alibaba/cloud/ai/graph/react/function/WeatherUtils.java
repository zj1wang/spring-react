package com.alibaba.cloud.ai.graph.react.function;

import org.springframework.stereotype.Component;

@Component
public class WeatherUtils {

    private final WeatherService weatherService;

    public WeatherUtils(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public String getWeather(String city) {
        return weatherService.getWeather(city);
    }
}