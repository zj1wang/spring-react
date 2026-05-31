package com.alibaba.cloud.ai.graph.react.service;

import com.alibaba.cloud.ai.graph.react.function.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class DashScopeChatService {

    private static final Logger logger = LoggerFactory.getLogger(DashScopeChatService.class);

    @Value("${spring.ai.openai.base-url}")
    private String baseUrl;

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.chat.options.model}")
    private String model;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final WeatherService weatherService;

    public DashScopeChatService(RestTemplate restTemplate, WeatherService weatherService) {
        this.restTemplate = restTemplate;
        this.weatherService = weatherService;
        this.objectMapper = new ObjectMapper();
    }

    public String chat(String message) {
        String url = baseUrl + "/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        List<Map<String, Object>> messages = new ArrayList<>();
        Map<String, Object> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", message);
        messages.add(userMessage);

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", messages);
        body.put("tools", getTools());
        body.put("tool_choice", "auto");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            String response = restTemplate.postForObject(url, request, String.class);
            return processResponse(response, messages);
        } catch (Exception e) {
            logger.error("Chat request failed: {}", e.getMessage());
            return "聊天服务暂时不可用，请稍后重试。";
        }
    }

    private List<Map<String, Object>> getTools() {
        List<Map<String, Object>> tools = new ArrayList<>();

        Map<String, Object> weatherTool = new HashMap<>();
        weatherTool.put("type", "function");

        Map<String, Object> function = new HashMap<>();
        function.put("name", "getWeather");
        function.put("description", "获取指定城市的天气信息");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("type", "object");

        Map<String, Object> properties = new HashMap<>();
        Map<String, Object> cityProp = new HashMap<>();
        cityProp.put("type", "string");
        cityProp.put("description", "城市名称");
        properties.put("city", cityProp);

        Map<String, Object> daysProp = new HashMap<>();
        daysProp.put("type", "integer");
        daysProp.put("description", "天气预报天数，范围1-14");
        properties.put("days", daysProp);

        parameters.put("properties", properties);
        parameters.put("required", Arrays.asList("city", "days"));

        function.put("parameters", parameters);
        weatherTool.put("function", function);

        tools.add(weatherTool);
        return tools;
    }

    private String processResponse(String response, List<Map<String, Object>> messages) {
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode choices = root.get("choices");
            
            if (choices != null && choices.isArray() && choices.size() > 0) {
                JsonNode choice = choices.get(0);
                JsonNode message = choice.get("message");
                
                if (message != null) {
                    JsonNode toolCalls = message.get("tool_calls");
                    
                    if (toolCalls != null && toolCalls.isArray() && toolCalls.size() > 0) {
                        for (JsonNode toolCall : toolCalls) {
                            String toolName = toolCall.get("function").get("name").asText();
                            String argsJson = toolCall.get("function").get("arguments").asText();
                            
                            String toolResult = callTool(toolName, argsJson);
                            
                            Map<String, Object> assistantMessage = new HashMap<>();
                            assistantMessage.put("role", "assistant");
                            assistantMessage.put("content", null);
                            assistantMessage.put("tool_calls", Collections.singletonList(toolCall));
                            messages.add(assistantMessage);

                            Map<String, Object> toolMessage = new HashMap<>();
                            toolMessage.put("role", "tool");
                            toolMessage.put("tool_call_id", toolCall.get("id").asText());
                            toolMessage.put("content", toolResult);
                            messages.add(toolMessage);
                        }

                        return callChatWithTools(messages);
                    } else {
                        return message.get("content").asText();
                    }
                }
            }
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse response: {}", e.getMessage());
        }
        return response;
    }

    private String callTool(String toolName, String argsJson) {
        try {
            JsonNode args = objectMapper.readTree(argsJson);
            String city = args.has("city") ? args.get("city").asText() : "";
            int days = args.has("days") ? args.get("days").asInt() : 1;

            if ("getWeather".equals(toolName) && !city.isEmpty()) {
                WeatherService.Request request = new WeatherService.Request(city, days);
                WeatherService.Response response = weatherService.apply(request);
                if (response != null) {
                    return objectMapper.writeValueAsString(Map.of(
                        "city", response.city(),
                        "current", response.current(),
                        "forecastDays", response.forecastDays()
                    ));
                }
            }
        } catch (Exception e) {
            logger.error("Tool call failed: {}", e.getMessage());
        }
        return "{}";
    }

    private String callChatWithTools(List<Map<String, Object>> messages) {
        String url = baseUrl + "/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", messages);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            String response = restTemplate.postForObject(url, request, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode choices = root.get("choices");
            
            if (choices != null && choices.isArray() && choices.size() > 0) {
                JsonNode message = choices.get(0).get("message");
                if (message != null && message.has("content")) {
                    return message.get("content").asText();
                }
            }
            return response;
        } catch (Exception e) {
            logger.error("Chat request failed: {}", e.getMessage());
            return "聊天服务暂时不可用，请稍后重试。";
        }
    }
}
