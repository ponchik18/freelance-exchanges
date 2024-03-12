package com.bsuir.dto;

import java.util.List;

public record FreelancerResponse(
        Long id,
        String firstName,
        String lastName,
        String profilePicture,
        String email,
        String userId,
        List<ResumeResponse> resumes
) {
}