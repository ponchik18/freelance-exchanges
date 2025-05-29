package com.bsuir.dto.job;

import com.bsuir.dto.skill.SkillResponse;
import com.bsuir.enums.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobSimpleResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private BigDecimal budget;
    private JobStatus jobStatus;
    private List<SkillResponse> skills;
}