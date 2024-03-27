package com.bsuir.dto.skill;

import lombok.Builder;

@Builder
public record SkillResponse(
        Long id,
        String name
) {
}