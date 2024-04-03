package com.bsuir.dto.customer;

import lombok.Builder;

@Builder
public record CustomerResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String profilePicture,
        String userId
) {
}