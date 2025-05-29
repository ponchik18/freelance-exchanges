package com.bsuir.service;

import com.bsuir.dto.user.UserRepresentation;
import com.bsuir.entity.Job;
import com.bsuir.entity.Proposal;
import com.bsuir.enums.ProposalStatus;
import com.bsuir.exception.DuplicateProposalException;
import com.bsuir.exception.ProposalNotFoundException;
import com.bsuir.feign.KeycloakFeignClient;
import com.bsuir.repository.JobRepository;
import com.bsuir.repository.ProposalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProposalService {
    private final ProposalRepository proposalRepository;
    private final KeycloakFeignClient keycloakFeignClient;
    private final JobService jobService;
    private final JobRepository jobRepository;

    public Proposal create(Proposal proposal) {
        proposal.setStatus(ProposalStatus.CREATED);
        jobService.getById(proposal.getJob().getId());

        UserRepresentation userRepresentation = keycloakFeignClient.getUser();
        proposal.setFreelancerId(userRepresentation.getUserId());

        if(proposalRepository.existsByFreelancerIdAndJobId(userRepresentation.getUserId(), proposal.getJob().getId())){
            throw new DuplicateProposalException();
        }

        return proposalRepository.save(proposal);
    }

    public Proposal getById(long id) {
        return proposalRepository.findById(id)
                .orElseThrow(() -> new ProposalNotFoundException(id));
    }

    public Proposal accept(long id) {
        Proposal proposal = getById(id);
        Job job = jobService.getById(proposal.getJob().getId());
        for(Proposal p : job.getProposals()){
            p.setStatus(ProposalStatus.REJECTED);
            proposalRepository.save(p);
        }
        proposal.setStatus(ProposalStatus.ACCEPTED);

        return proposalRepository.save(proposal);
    }

    public Proposal reject(long id) {
        Proposal proposal = getById(id);
        proposal.setStatus(ProposalStatus.REJECTED);

        return proposalRepository.save(proposal);
    }

    public List<Proposal> getAllFroFreelancer() {
        String userId = keycloakFeignClient.getUser()
                .getUserId();

        return proposalRepository.findAllByFreelancerId(userId);
    }
}