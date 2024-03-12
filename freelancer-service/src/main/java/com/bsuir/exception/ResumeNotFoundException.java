package com.bsuir.exception;

import static com.bsuir.constant.FreelanceServiceConstant.Error.RESUME_NOT_FOUND;

public class ResumeNotFoundException extends RuntimeException {
    public ResumeNotFoundException(long id) {
        super(String.format(RESUME_NOT_FOUND, id));
    }
}