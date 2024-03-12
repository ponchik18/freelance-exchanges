package com.bsuir.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.bsuir.constant.FreelanceServiceConstant.Validation.Message.NOT_VALID_FILED;

@RestControllerAdvice
public class FreelancerExceptionHandler {

    @ExceptionHandler({FreelancerNotFoundException.class, ResumeNotFoundException.class, NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessageResponse handeNotFoundException(Exception exception) {
        return ErrorMessageResponse.builder()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .timestamp(new Date())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ValidationErrorResponse.builder()
                .fieldErrors(errors)
                .timestamp(new Date())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(NOT_VALID_FILED)
                .build();
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessageResponse handleDuplicateKeyException(Exception exception) {
        return ErrorMessageResponse.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .timestamp(new Date())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessageResponse handleAccessDeniedException(AccessDeniedException exception) {
        return ErrorMessageResponse.builder()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .timestamp(new Date())
                .message(exception.getMessage())
                .build();
    }

}