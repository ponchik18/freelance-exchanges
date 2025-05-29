package com.bsuir.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    private MessageStatus status;
    private String recipientName;
    private String recipientEmail;
    private String message;
    private JobOverview job;
}
