package com.alibaba.cloud.ai.graph.react;

import com.alibaba.cloud.ai.graph.react.service.DashScopeChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/react")
public class ReactController {

    private final DashScopeChatService chatService;

    public ReactController(DashScopeChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam String message) {
        String result = chatService.chat(message);
        return ResponseEntity.ok(result);
    }
}