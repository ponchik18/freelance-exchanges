package com.bsuir.util;

import com.bsuir.exception.ErrorMessageResponse;
import com.bsuir.exception.ResourceNotFoundException;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public class WebClientErrorHandler {
    public static Mono<? extends Throwable> handle4xxError(ClientResponse response) {
        return response.bodyToMono(ErrorMessageResponse.class)
                .flatMap(errorResponse -> Mono.error(new ResourceNotFoundException(errorResponse.message())));
    }

    public static Mono<? extends Throwable> handle5xxError(ClientResponse response) {
        return response.bodyToMono(ErrorMessageResponse.class)
                .flatMap(errorResponse -> Mono.error(new Exception(errorResponse.message())));
    }
}