package com.bsuir.dto.job;

import com.bsuir.dto.customer.CustomerResponse;
import com.bsuir.dto.proposal.ProposalResponse;
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
public class JobResponse {
    private Long id;
    private CustomerResponse customer;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private BigDecimal budget;
    private JobStatus jobStatus;
    private String jobReference;
    private List<SkillResponse> skills;
    private List<ProposalResponse> proposals;
}