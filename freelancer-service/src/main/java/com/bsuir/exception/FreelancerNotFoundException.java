package com.bsuir.exception;

import static com.bsuir.constant.FreelanceServiceConstant.Error.FREELANCER_NOT_FOUND;

public class FreelancerNotFoundException extends RuntimeException {
    public FreelancerNotFoundException(long id) {
        super(String.format(FREELANCER_NOT_FOUND, id));
    }
}