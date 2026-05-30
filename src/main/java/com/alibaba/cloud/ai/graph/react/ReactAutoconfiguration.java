package com.alibaba.cloud.ai.graph.react;

import java.util.concurrent.TimeUnit;

import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.GraphRepresentation;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.Timeout;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.tool.resolution.ToolCallbackResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class ReactAutoconfiguration {

    @Bean
    public ReactAgent normalReactAgent(ChatModel chatModel, ToolCallbackResolver resolver) throws GraphStateException {
        ChatClient chatClient = ChatClient.builder(chatModel)
            .defaultToolNames("getWeatherFunction")
            .defaultAdvisors(new SimpleLoggerAdvisor())
            .defaultOptions(OpenAiChatOptions.builder().internal(true).build())
            .build();

        ReactAgent reactAgent = new ReactAgent(chatClient);
        reactAgent.setToolCallbackResolver(resolver);
        return reactAgent;
    }

    @Bean
    @Qualifier("reactAgentRestClient")
    public RestClient reactAgentRestClient() {
        RequestConfig requestConfig = RequestConfig.custom()
            .setConnectionRequestTimeout(Timeout.of(30, TimeUnit.SECONDS))
            .setConnectTimeout(Timeout.of(30, TimeUnit.SECONDS))
            .setResponseTimeout(Timeout.of(60, TimeUnit.SECONDS))
            .build();

        HttpClient httpClient = HttpClients.custom()
            .setDefaultRequestConfig(requestConfig)
            .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

        return RestClient.builder()
            .requestFactory(factory)
            .build();
    }
}