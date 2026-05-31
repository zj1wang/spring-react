package com.alibaba.cloud.ai.graph.react.function;

import java.util.HashMap;
import java.util.Map;

public class WeatherService {

    private final Map<String, String> weatherData = new HashMap<>();

    public WeatherService() {
        initWeatherData();
    }

    private void initWeatherData() {
        weatherData.put("北京", "{\"city\":\"北京\",\"temp\":25,\"description\":\"晴朗\",\"humidity\":45,\"wind\":\"西北风3级\"}");
        weatherData.put("上海", "{\"city\":\"上海\",\"temp\":28,\"description\":\"多云\",\"humidity\":65,\"wind\":\"东南风2级\"}");
        weatherData.put("广州", "{\"city\":\"广州\",\"temp\":32,\"description\":\"雷阵雨\",\"humidity\":80,\"wind\":\"南风4级\"}");
        weatherData.put("深圳", "{\"city\":\"深圳\",\"temp\":30,\"description\":\"多云转晴\",\"humidity\":70,\"wind\":\"东风2级\"}");
        weatherData.put("杭州", "{\"city\":\"杭州\",\"temp\":26,\"description\":\"小雨\",\"humidity\":75,\"wind\":\"东北风3级\"}");
        weatherData.put("成都", "{\"city\":\"成都\",\"temp\":22,\"description\":\"阴\",\"humidity\":85,\"wind\":\"北风1级\"}");
        weatherData.put("武汉", "{\"city\":\"武汉\",\"temp\":27,\"description\":\"晴转多云\",\"humidity\":55,\"wind\":\"西南风2级\"}");
        weatherData.put("南京", "{\"city\":\"南京\",\"temp\":24,\"description\":\"多云\",\"humidity\":60,\"wind\":\"东风3级\"}");
    }

    public String getWeather(String city) {
        String weather = weatherData.get(city);
        if (weather != null) {
            return weather;
        }
        return "{\"city\":\"" + city + "\",\"temp\":20,\"description\":\"晴朗\",\"humidity\":50,\"wind\":\"微风\"}";
    }
}