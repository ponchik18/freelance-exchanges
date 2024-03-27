package com.bsuir.dto.proposal;

import com.bsuir.dto.freelancer.FreelancerResponse;
import com.bsuir.enums.ProposalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProposalResponse {
    private Long id;
    private FreelancerResponse freelancer;
    private BigDecimal suggestedBudget;
    private LocalDateTime createdAt;
    private ProposalStatus status;
    private String coveringLetter;
}