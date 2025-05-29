package com.bsuir.config;

import com.bsuir.exception.ErrorMessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.io.InputStream;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        ErrorMessageResponse message;
        System.out.println(response);
        if (response.body() != null) {
            return new Exception(response.toString());
        }
        try (InputStream bodyIs = response.body().asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(bodyIs, ErrorMessageResponse.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }
        return new Exception(message.message());
    }
}