package com.bsuir.feign;

import com.bsuir.config.FeignClientConfiguration;
import com.bsuir.dto.message.ChatRequest;
import com.bsuir.dto.message.ChatResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "chat-service",
        configuration = FeignClientConfiguration.class,
        path = "/api/chats"
)
public interface ChatFeignClient {
    @PostMapping
    ChatResponse createMessage(@RequestBody ChatRequest chatRequest);
}