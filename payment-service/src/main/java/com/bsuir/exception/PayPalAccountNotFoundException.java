package com.bsuir.exception;

import static com.bsuir.constants.PaymentServiceConstants.Error.Message.PAY_PAL_NOT_FOUND;

public class PayPalAccountNotFoundException extends RuntimeException {
    public PayPalAccountNotFoundException(String userId) {
        super(String.format(PAY_PAL_NOT_FOUND, userId));
    }
}