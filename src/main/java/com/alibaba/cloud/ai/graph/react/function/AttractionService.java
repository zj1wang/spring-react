package com.alibaba.cloud.ai.graph.react.function;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class AttractionService implements Function<AttractionService.Request, AttractionService.Response> {

    private static final Logger logger = LoggerFactory.getLogger(AttractionService.class);

    @Override
    public Response apply(Request request) {
        if (request == null || !StringUtils.hasText(request.city())) {
            logger.error("Invalid request: city is required.");
            return null;
        }
        try {
            return getAttractionsMock(request);
        } catch (Exception e) {
            logger.error("Failed to fetch attractions data: {}", e.getMessage());
            return null;
        }
    }

    private Response getAttractionsMock(Request request) {
        String city = request.city();
        int limit = request.limit() > 0 ? request.limit() : 5;

        if (Objects.equals("杭州", city)) {
            List<Map<String, Object>> attractions = new ArrayList<>();
            attractions.add(createAttraction("西湖", "杭州最著名的景点，风景秀丽", 4.9));
            attractions.add(createAttraction("灵隐寺", "千年古刹，佛教圣地", 4.8));
            attractions.add(createAttraction("雷峰塔", "白娘子传说所在地", 4.7));
            attractions.add(createAttraction("千岛湖", "湖中有1078个岛屿", 4.8));
            attractions.add(createAttraction("宋城", "宋代主题文化乐园", 4.6));
            return new Response(city, attractions.subList(0, Math.min(limit, attractions.size())));
        } else if (Objects.equals("上海", city)) {
            List<Map<String, Object>> attractions = new ArrayList<>();
            attractions.add(createAttraction("外滩", "上海标志性景点", 4.9));
            attractions.add(createAttraction("东方明珠", "上海地标建筑", 4.7));
            attractions.add(createAttraction("豫园", "江南古典园林", 4.6));
            attractions.add(createAttraction("上海迪士尼", "大型主题乐园", 4.8));
            attractions.add(createAttraction("南京路", "繁华商业街", 4.5));
            return new Response(city, attractions.subList(0, Math.min(limit, attractions.size())));
        } else if (Objects.equals("南京", city)) {
            List<Map<String, Object>> attractions = new ArrayList<>();
            attractions.add(createAttraction("中山陵", "孙中山先生陵墓", 4.8));
            attractions.add(createAttraction("夫子庙", "历史文化街区", 4.7));
            attractions.add(createAttraction("明孝陵", "明朝开国皇帝朱元璋陵墓", 4.8));
            attractions.add(createAttraction("玄武湖", "南京最大的城内公园", 4.6));
            attractions.add(createAttraction("总统府", "中国近代史遗址博物馆", 4.7));
            return new Response(city, attractions.subList(0, Math.min(limit, attractions.size())));
        } else if (Objects.equals("北京", city)) {
            List<Map<String, Object>> attractions = new ArrayList<>();
            attractions.add(createAttraction("故宫", "世界上现存规模最大的宫殿建筑群", 4.9));
            attractions.add(createAttraction("天安门广场", "中国的心脏地带", 4.8));
            attractions.add(createAttraction("八达岭长城", "世界文化遗产", 4.9));
            attractions.add(createAttraction("颐和园", "皇家园林博物馆", 4.8));
            attractions.add(createAttraction("天坛", "明清两代皇帝祭天场所", 4.7));
            return new Response(city, attractions.subList(0, Math.min(limit, attractions.size())));
        } else if (Objects.equals("广州", city)) {
            List<Map<String, Object>> attractions = new ArrayList<>();
            attractions.add(createAttraction("广州塔", "小蛮腰，广州地标", 4.7));
            attractions.add(createAttraction("白云山", "羊城第一秀", 4.6));
            attractions.add(createAttraction("陈家祠", "岭南建筑艺术明珠", 4.7));
            attractions.add(createAttraction("长隆欢乐世界", "大型主题乐园", 4.8));
            attractions.add(createAttraction("沙面岛", "欧式风情街区", 4.6));
            return new Response(city, attractions.subList(0, Math.min(limit, attractions.size())));
        } else {
            List<Map<String, Object>> attractions = new ArrayList<>();
            attractions.add(createAttraction("市中心广场", "城市中心地标", 4.0));
            attractions.add(createAttraction("人民公园", "休闲好去处", 3.8));
            attractions.add(createAttraction("历史博物馆", "了解城市历史", 4.2));
            return new Response(city, attractions.subList(0, Math.min(limit, attractions.size())));
        }
    }

    private Map<String, Object> createAttraction(String name, String description, double rating) {
        Map<String, Object> attraction = new HashMap<>();
        attraction.put("name", name);
        attraction.put("description", description);
        attraction.put("rating", rating);
        return attraction;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonClassDescription("Attraction Service API request")
    public record Request(
            @JsonProperty(required = true, value = "city") @JsonPropertyDescription("city name") String city,
            @JsonProperty(value = "limit") @JsonPropertyDescription("Number of attractions to return, default 5") int limit) {
    }

    @JsonClassDescription("Attraction Service API response")
    public record Response(
            @JsonProperty(required = true, value = "city") @JsonPropertyDescription("city name") String city,
            @JsonProperty(required = true,
                    value = "attractions") @JsonPropertyDescription("List of attractions") List<Map<String, Object>> attractions) {
    }
}
