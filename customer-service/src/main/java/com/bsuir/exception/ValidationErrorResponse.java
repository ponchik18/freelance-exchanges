package com.bsuir.exception;

import lombok.Builder;

import java.util.Date;
import java.util.Map;

@Builder
public record ValidationErrorResponse(
        Map<String, String> fieldErrors,
        Integer statusCode,
        Date timestamp,
        String message
) {
}