package com.bsuir.mapper;

import com.bsuir.dto.customer.CustomerResponse;
import com.bsuir.dto.job.JobResponse;
import com.bsuir.dto.proposal.ProposalResponse;
import com.bsuir.entity.Job;
import com.bsuir.entity.Proposal;
import com.bsuir.feign.FreelancerFeignClient;
import com.bsuir.repository.ProposalRepository;
import com.bsuir.webclient.CustomerWebClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JobResponseMapper implements Function<Job, JobResponse> {
    private final CustomerWebClient customerWebClient;
    private final FreelancerFeignClient freelancerFeignClient;
    private final ProposalRepository proposalRepository;
    private final ProposalMapper proposalMapper;
    private final JobMapper jobMapper;

    @Override
    public JobResponse apply(Job job) {
        CustomerResponse customerResponse = customerWebClient.getCustomerById(job.getCustomerId());

        List<Proposal> proposals = job.getProposals();
        List<ProposalResponse> proposalResponses = proposals.stream()
                .map(proposal -> {
                    ProposalResponse proposalResponse = proposalMapper.toDto(proposal);
                    proposalResponse.setFreelancer(freelancerFeignClient.getFreelancerById(proposal.getFreelancerId()));
                    return proposalResponse;
                })
                .toList();

        JobResponse jobResponse = jobMapper.toResponse(job);
        jobResponse.setCustomer(customerResponse);
        jobResponse.setProposals(proposalResponses);
        jobResponse.setJobStatus(job.getStatus().name());

        return jobResponse;
    }
}