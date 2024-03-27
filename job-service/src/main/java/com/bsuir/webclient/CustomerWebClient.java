package com.bsuir.webclient;

import com.bsuir.dto.customer.CustomerCreateRequest;
import com.bsuir.dto.customer.CustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import com.bsuir.util.WebClientErrorHandler;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerWebClient {
    private final WebClient webClient;

    public CustomerResponse getCustomerById(String id) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/{id}")
                        .build(id))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, WebClientErrorHandler::handle4xxError)
                .onStatus(HttpStatusCode::is5xxServerError, WebClientErrorHandler::handle5xxError)
                .bodyToMono(CustomerResponse.class)
                .block();
    }
}