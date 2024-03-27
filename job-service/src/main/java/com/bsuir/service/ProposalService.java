package com.bsuir.service;

import com.bsuir.dto.user.UserRepresentation;
import com.bsuir.entity.Proposal;
import com.bsuir.enums.ProposalStatus;
import com.bsuir.exception.ProposalNotFoundException;
import com.bsuir.feign.KeycloakFeignClient;
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

    public Proposal create(Proposal proposal) {
        proposal.setStatus(ProposalStatus.CREATED);
        jobService.getById(proposal.getId());

        UserRepresentation userRepresentation = keycloakFeignClient.getUser();
        proposal.setFreelancerId(userRepresentation.getId());

        return proposalRepository.save(proposal);
    }

    public Proposal getById(long id) {
        return proposalRepository.findById(id)
                .orElseThrow(() -> new ProposalNotFoundException(id));
    }

    public Proposal accept(long id) {
        Proposal proposal = getById(id);
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
                .getId();

        return proposalRepository.findAllByFreelancerId(userId);
    }
}