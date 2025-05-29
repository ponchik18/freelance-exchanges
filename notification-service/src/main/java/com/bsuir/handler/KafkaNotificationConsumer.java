package com.bsuir.handler;

import com.bsuir.dto.kafka.Message;
import com.bsuir.service.ProcessMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaNotificationConsumer {

    private final ProcessMessageService processMessageService;

    @KafkaListener(topics = "notification")
    public void listen(Message message) {
        processMessageService.sendJobChangeStatusMail(message);
    }
}
