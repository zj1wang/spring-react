package com.alibaba.cloud.ai.graph.react.function;

import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.chat.model.ToolResponse;
import org.springframework.ai.tool.AbstractTool;
import org.springframework.ai.tool.ToolResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("getWeatherFunction")
public class WeatherUtils extends AbstractTool {

    private final WeatherService weatherService;

    @Autowired
    public WeatherUtils(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Override
    public String getName() {
        return "getWeatherFunction";
    }

    @Override
    public String getDescription() {
        return "根据城市名称获取天气信息。参数：city（城市名称，字符串类型）";
    }

    @Override
    public ToolResult call(Map<String, Object> arguments, ToolContext context) {
        String city = (String) arguments.get("city");
        String weatherData = weatherService.getWeather(city);
        return new ToolResult(new ToolResponse(getName(), weatherData));
    }
}