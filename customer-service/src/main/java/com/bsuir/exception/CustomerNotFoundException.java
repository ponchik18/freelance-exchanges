package com.bsuir.exception;

import static com.bsuir.constant.CustomerServiceConstant.Error.CUSTOMER_NOT_FOUND;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String id) {
        super(String.format(CUSTOMER_NOT_FOUND, id));
    }
}