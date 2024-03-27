package com.bsuir.exception;

import static com.bsuir.constant.JobServiceConstant.Error.SKILL_NOT_FOUND;

public class SkillNotFoundException extends RuntimeException {
    public SkillNotFoundException(Long id) {
        super(String.format(SKILL_NOT_FOUND, id));
    }
}