package com.bsuir.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private Long id;
    private String senderId;
    private String recipientId;
    private String message;
    private LocalDateTime createdAt;
    private Boolean isRead;
}