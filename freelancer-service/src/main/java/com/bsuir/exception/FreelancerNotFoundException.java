package com.bsuir.exception;

import static com.bsuir.constant.FreelanceServiceConstant.Error.FREELANCER_WITH_NOT_FOUND;

public class FreelancerNotFoundException extends RuntimeException {
    public FreelancerNotFoundException(String id) {
        super(String.format(FREELANCER_WITH_NOT_FOUND, id));
    }

    public FreelancerNotFoundException() {
        super(FREELANCER_WITH_NOT_FOUND);
    }
}