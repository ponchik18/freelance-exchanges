package com.bsuir.dto.skill;

import com.bsuir.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkillForAnalyticsResponse {
    private Skill skill;
    private Long count;
    private BigDecimal percent;
}
