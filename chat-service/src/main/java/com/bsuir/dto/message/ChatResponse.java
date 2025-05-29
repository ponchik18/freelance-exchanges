package com.bsuir.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {
    private Long id;
    private String firstUserId;
    private String secondUserId;
    private List<MessageResponse> messages;
}