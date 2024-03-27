package com.bsuir.exception;

import static com.bsuir.constant.JobServiceConstant.Error.JOB_NOT_FOUND;

public class JobNotFoundException extends RuntimeException {
    public JobNotFoundException(Long id) {
        super(String.format(JOB_NOT_FOUND, id));
    }
}