package com.bsuir.service;

import com.bsuir.dto.message.ChatRequest;
import com.bsuir.dto.message.MessageRequest;
import com.bsuir.entity.Chat;
import com.bsuir.entity.Message;
import com.bsuir.repository.ChatRepository;
import com.bsuir.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;


    public Chat createChat(ChatRequest chatRequest) {
        Chat chat = chatRepository.findByUserIds(chatRequest.getTo(), chatRequest.getFrom())
                .orElse(null);
        if(chat !=  null) {
            return chat;
        }

        chat = new Chat();
        chat.setFirstUserId(chatRequest.getTo());
        chat.setSecondUserId(chatRequest.getFrom());
        return chatRepository.save(chat);
    }

    public Chat getByUsersId(String firstUserId, String secondUserId) {
        return chatRepository.findByUserIds(firstUserId, secondUserId)
                .orElseThrow();
    }

    public Message sendMessage(MessageRequest messageRequest) {
        Chat chat = getByUsersId(messageRequest.getRecipientId(), messageRequest.getSenderId());
        Message message = Message.builder()
                .recipientId(messageRequest.getRecipientId())
                .senderId(messageRequest.getSenderId())
                .message(messageRequest.getMessage())
                .chat(chat)
                .build();

        return messageRepository.save(message);
    }
}