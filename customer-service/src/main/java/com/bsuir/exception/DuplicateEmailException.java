package com.bsuir.exception;

import jakarta.validation.constraints.NotBlank;

import static com.bsuir.constant.CustomerServiceConstant.Error.DUPLiCATE_EMAIL;
import static com.bsuir.constant.CustomerServiceConstant.Validation.Message.NOT_EMPTY;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(@NotBlank(message = NOT_EMPTY) String email) {
        super(String.format(DUPLiCATE_EMAIL, email));
    }
}