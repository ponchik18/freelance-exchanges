package com.bsuir.exception;

import static com.bsuir.constant.JobServiceConstant.Error.PROPOSAL_NOT_FOUND;

public class ProposalNotFoundException extends RuntimeException {
    public ProposalNotFoundException(long id) {
        super(String.format(PROPOSAL_NOT_FOUND, id));
    }
}