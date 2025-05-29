package com.bsuir.service;

import com.bsuir.dto.kafka.Message;
import com.bsuir.dto.mail.EmailDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProcessMessageService {

    private final SpringTemplateEngine templateEngine;
    private final EmailServiceMessage emailServiceMessage;

    public void sendJobChangeStatusMail(Message message) {
        String content = getHtmlContent(generateProperty(message), "job.html");
        EmailDetails emailDetails = EmailDetails.builder()
                .subject("Обновление статуса заказа")
                .recipient(message.getRecipientEmail())
                .messageBody(content)
                .build();
        emailServiceMessage.sendEmail(emailDetails);
    }

    private String getHtmlContent(Map<String, Object> properties, String templateName) {
        Context context = new Context();
        context.setVariables(properties);
        return templateEngine.process(templateName, context);
    }

    private Map<String, Object> generateProperty(Message message) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("recipientName", message.getRecipientName());
        properties.put("message", message.getMessage());

        Map<String, Object> jobData = new HashMap<>();
        jobData.put("title", message.getJob().getTitle());
        jobData.put("description", message.getJob().getDescription());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy 'г.,' HH:mm", new Locale("ru"));

        String formattedDate = message.getJob().getCreatedAt().format(formatter);
        jobData.put("createdAt", formattedDate);
        jobData.put("budget", "%.2f $".formatted(message.getJob().getBudget().doubleValue()));

        properties.put("job", jobData);
        return properties;
    }
}
