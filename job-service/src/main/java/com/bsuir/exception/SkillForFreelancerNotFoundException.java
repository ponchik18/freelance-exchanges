package com.bsuir.exception;

import static com.bsuir.constant.JobServiceConstant.Error.SKILL_FOR_FREELANCER_NOT_FOUND;

public class SkillForFreelancerNotFoundException extends RuntimeException {
    public SkillForFreelancerNotFoundException(String freelancerId, Long skillId) {
        super(String.format(SKILL_FOR_FREELANCER_NOT_FOUND, skillId, freelancerId));
    }
}