package com.bsuir.webclient;

import com.bsuir.dto.customer.CustomerCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import com.bsuir.util.WebClientErrorHandler;

@Service
@RequiredArgsConstructor
public class CustomerWebClient {
    private final WebClient webClient;

    public void createCustomer(CustomerCreateRequest customerCreateRequest) {
        webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(customerCreateRequest))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, WebClientErrorHandler::handle4xxError)
                .onStatus(HttpStatusCode::is5xxServerError, WebClientErrorHandler::handle5xxError)
                .bodyToMono(Object.class)
                .block();
    }
}