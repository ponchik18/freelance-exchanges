package com.bsuir.exception;

import static com.bsuir.constant.FreelanceServiceConstant.Error.DUPLiCATE_EMAIL;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super(String.format(DUPLiCATE_EMAIL, email));
    }
}