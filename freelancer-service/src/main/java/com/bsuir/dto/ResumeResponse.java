package com.bsuir.dto;

import lombok.Builder;

@Builder
public record ResumeResponse(
        Long id,
        String resumeName,
        String resumeContent,
        Long freelancerId
) {
}