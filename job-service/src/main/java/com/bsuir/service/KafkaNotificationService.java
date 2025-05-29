package com.bsuir.service;

import com.bsuir.dto.job.JobResponse;
import com.bsuir.dto.kafka.JobOverview;
import com.bsuir.dto.kafka.Message;
import com.bsuir.dto.kafka.MessageStatus;
import com.bsuir.dto.proposal.ProposalResponse;
import com.bsuir.enums.JobStatus;
import com.bsuir.enums.ProposalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaNotificationService {

    private final KafkaTemplate<String, Message> kafkaTemplate;

    private void sendToNotificationTopic(Message message) {
        String NOTIFICATION_TOPIC = "notification";
        kafkaTemplate.send(NOTIFICATION_TOPIC, message);
    }

    public void sendMessageToCustomer(MessageStatus messageStatus, JobResponse jobResponse) {
        Message message = Message.builder()
                .recipientName("%s %s".formatted(jobResponse.getCustomer().lastName(), jobResponse.getCustomer().firstName()))
                .recipientEmail(jobResponse.getCustomer().email())
                .message(generateMessage(jobResponse.getJobStatus(), false))
                .status(messageStatus)
                .job(mapToJobOverview(jobResponse))
                .build();
        sendToNotificationTopic(message);
    }

    public void sendMessageToFreelancer(MessageStatus messageStatus, JobResponse jobResponse) {
        ProposalResponse proposalResponse = findAcceptedProposal(jobResponse);
        Message message = Message.builder()
                .recipientName("%s %s".formatted(proposalResponse.getFreelancer().lastName(), proposalResponse.getFreelancer().firstName()))
                .recipientEmail(proposalResponse.getFreelancer().email())
                .message(generateMessage(jobResponse.getJobStatus(), true))
                .status(messageStatus)
                .job(mapToJobOverview(jobResponse))
                .build();
        sendToNotificationTopic(message);
    }

    private JobOverview mapToJobOverview(JobResponse job) {
        return JobOverview.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .budget(job.getBudget())
                .createdAt(job.getCreatedAt())
                .build();
    }

    private String generateMessage(JobStatus jobStatus, boolean isFreelancer) {
        switch (jobStatus) {
            case CREATED:
                return isFreelancer
                        ? "Новый проект создан. Проверьте доступные задания в вашем списке."
                        : "Ваш проект успешно создан и ожидает исполнителей.";

            case WORKED:
                return isFreelancer
                        ? "Вы назначены на выполнение проекта. Пожалуйста, приступайте к работе."
                        : "Исполнитель приступил к работе над вашим проектом.";

            case FINISH:
                return isFreelancer
                        ? "Вы завершили проект. Дождитесь подтверждения от заказчика."
                        : "Исполнитель завершил проект. Проверьте результат.";

            case PAID:
                return isFreelancer
                        ? "Оплата за проект успешно получена. Проверьте ваш баланс."
                        : "Вы успешно оплатили проект. Спасибо за сотрудничество!";

            case CANCELLED:
                return isFreelancer
                        ? "К сожалению, проект был отменен. Следите за новыми заданиями."
                        : "Ваш проект был отменен. Надеемся, мы сможем помочь вам в будущем.";

            default:
                return "Неизвестный статус проекта.";
        }
    }

    private ProposalResponse findAcceptedProposal(JobResponse job) {
        return job.getProposals().stream()
                .filter((proposal -> proposal.getStatus() == ProposalStatus.ACCEPTED))
                .findFirst().orElseThrow();
    }

}
