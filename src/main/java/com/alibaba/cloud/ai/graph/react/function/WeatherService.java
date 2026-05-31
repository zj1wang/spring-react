package com.alibaba.cloud.ai.graph.react.function;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class WeatherService implements Function<WeatherService.Request, WeatherService.Response> {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Override
    public Response apply(Request request) {
        if (request == null || !StringUtils.hasText(request.city())) {
            logger.error("Invalid request: city is required.");
            return null;
        }
        try {
            return doGetWeatherMock(request);
        } catch (Exception e) {
            logger.error("Failed to fetch weather data: {}", e.getMessage());
            return null;
        }
    }

    private Response doGetWeatherMock(Request request) {
        if (Objects.equals("杭州", request.city())) {
            return new Response(request.city(), Map.of("temp", 25, "condition", "Sunny"),
                    List.of(Map.of("date", "2025-05-27", "high", 28, "low", 20)));
        } else if (Objects.equals("上海", request.city())) {
            return new Response(request.city(), Map.of("temp", 26, "condition", "Sunny"),
                    List.of(Map.of("date", "2025-05-27", "high", 29, "low", 21)));
        } else if (Objects.equals("南京", request.city())) {
            return new Response(request.city(), Map.of("temp", 18, "condition", "cloudy"),
                    List.of(Map.of("date", "2025-05-27", "high", 18, "low", 10)));
        } else if (Objects.equals("北京", request.city())) {
            return new Response(request.city(), Map.of("temp", 22, "condition", "多云"),
                    List.of(Map.of("date", "2025-05-27", "high", 25, "low", 18)));
        } else if (Objects.equals("广州", request.city())) {
            return new Response(request.city(), Map.of("temp", 32, "condition", "晴"),
                    List.of(Map.of("date", "2025-05-27", "high", 35, "low", 26)));
        } else {
            return new Response(request.city(), Map.of("temp", -20, "condition", "Snowy"),
                    List.of(Map.of("date", "2025-05-27", "high", -10, "low", -30)));
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonClassDescription("Weather Service API request")
    public record Request(
            @JsonProperty(required = true, value = "city") @JsonPropertyDescription("city name") String city,
            @JsonProperty(required = true,
                    value = "days") @JsonPropertyDescription("Number of days of weather forecast. Value ranges from 1 to 14") int days) {
    }

    @JsonClassDescription("Weather Service API response")
    public record Response(
            @JsonProperty(required = true, value = "city") @JsonPropertyDescription("city name") String city,
            @JsonProperty(required = true,
                    value = "current") @JsonPropertyDescription("Current weather info") Map<String, Object> current,
            @JsonProperty(required = true,
                    value = "forecastDays") @JsonPropertyDescription("Forecast weather info") List<Map<String, Object>> forecastDays) {
    }
}
