package com.bsuir.dto.freelancer;

public record FreelancerResponse(
        Long id,
        String firstName,
        String lastName,
        String profilePicture,
        String email,
        String userId
) {
}