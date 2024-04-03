package com.bsuir.exception;

import static com.bsuir.constants.PaymentServiceConstants.Error.Message.DUPLICATE_VALUE;

public class DuplicateValueException extends RuntimeException {
    public DuplicateValueException(String field, String value) {
        super(String.format(DUPLICATE_VALUE, field, value));
    }
}