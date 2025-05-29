package com.bsuir.contrtoller;

import com.bsuir.dto.message.ChatRequest;
import com.bsuir.dto.message.ChatResponse;
import com.bsuir.dto.message.MessageRequest;
import com.bsuir.dto.message.MessageResponse;
import com.bsuir.entity.Chat;
import com.bsuir.entity.Message;
import com.bsuir.mapper.ChatMapper;
import com.bsuir.mapper.MessageMapper;
import com.bsuir.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ChatMapper chatMapper;
    private final MessageMapper messageMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChatResponse createChat(@RequestBody ChatRequest chatRequest) {
        Chat chat = chatService.createChat(chatRequest);
        return chatMapper.toDto(chat);
    }

    @GetMapping("/{firstUserId}/{secondUserId}")
    public ChatResponse getChat(@PathVariable String firstUserId, @PathVariable String secondUserId) {
        Chat chat = chatService.getByUsersId(firstUserId, secondUserId);
        return chatMapper.toDto(chat);
    }

    @PostMapping("/message")
    public MessageResponse sendMessage(@RequestBody MessageRequest messageRequest) {
        Message message = chatService.sendMessage(messageRequest);
        return messageMapper.toDto(message);
    }
}