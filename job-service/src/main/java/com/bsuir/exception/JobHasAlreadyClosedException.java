package com.bsuir.exception;

import static com.bsuir.constant.JobServiceConstant.Error.JOB_HAS_ALREADY_CLOSED;

public class JobHasAlreadyClosedException extends RuntimeException {
    public JobHasAlreadyClosedException(Long id) {
        super(String.format(JOB_HAS_ALREADY_CLOSED,id));
    }
}