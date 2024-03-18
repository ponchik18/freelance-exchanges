package com.bsuir.dto;

import lombok.Builder;

@Builder
public record UserResponse(
        String userId
) {
}