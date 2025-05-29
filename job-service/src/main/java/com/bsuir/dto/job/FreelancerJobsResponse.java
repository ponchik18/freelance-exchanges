package com.bsuir.dto.job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FreelancerJobsResponse {
    private List<JobResponse> createdJobs;
    private List<JobResponse> workedJobs;
    private List<JobResponse> finishJobs;
    private List<JobResponse> paidJobs;
    private List<JobResponse> cancelledJobs;
    private List<JobResponse> allJobs;
}
