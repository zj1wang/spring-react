package com.alibaba.cloud.ai.graph.react;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/react")
public class ReactController {

    private final ReactAgent reactAgent;

    public ReactController(ReactAgent reactAgent) {
        this.reactAgent = reactAgent;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam String message) {
        String result = reactAgent.generate(message);
        return ResponseEntity.ok(result);
    }
}