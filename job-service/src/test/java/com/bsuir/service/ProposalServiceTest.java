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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProposalServiceTest {

    @InjectMocks
    private ProposalService proposalService;

    @Mock
    private ProposalRepository proposalRepository;

    @Mock
    private KeycloakFeignClient keycloakFeignClient;

    @Mock
    private JobService jobService;

    @Mock
    private JobRepository jobRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProposal_WhenNoDuplicateExists() {
        Proposal proposal = new Proposal();
        proposal.setJob(new Job(1L));
        UserRepresentation userRepresentation = new UserRepresentation("freelancer1");

        when(jobService.getById(1L)).thenReturn(new Job(1L));
        when(keycloakFeignClient.getUser()).thenReturn(userRepresentation);
        when(proposalRepository.existsByFreelancerIdAndJobId("freelancer1", 1L)).thenReturn(false);
        when(proposalRepository.save(proposal)).thenReturn(proposal);

        Proposal result = proposalService.create(proposal);

        assertNotNull(result);
        assertEquals(ProposalStatus.CREATED, result.getStatus());
        verify(proposalRepository, times(1)).save(proposal);
    }

    @Test
    void testCreateProposal_WhenDuplicateExists() {
        Proposal proposal = new Proposal();
        proposal.setJob(new Job(1L));
        UserRepresentation userRepresentation = new UserRepresentation("freelancer1");

        when(jobService.getById(1L)).thenReturn(new Job(1L));
        when(keycloakFeignClient.getUser()).thenReturn(userRepresentation);
        when(proposalRepository.existsByFreelancerIdAndJobId("freelancer1", 1L)).thenReturn(true);

        assertThrows(DuplicateProposalException.class, () -> proposalService.create(proposal));
        verify(proposalRepository, never()).save(any());
    }

    @Test
    void testGetById_WhenProposalExists() {
        Proposal proposal = new Proposal();
        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal));

        Proposal result = proposalService.getById(1L);

        assertNotNull(result);
        verify(proposalRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_WhenProposalDoesNotExist() {
        when(proposalRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProposalNotFoundException.class, () -> proposalService.getById(1L));
        verify(proposalRepository, times(1)).findById(1L);
    }

    @Test
    void testAcceptProposal() {
        Proposal proposal = new Proposal();
        proposal.setId(1L);
        proposal.setJob(new Job(1L));
        Job job = new Job(1L);
        List<Proposal> proposals = new ArrayList<>();
        proposals.add(proposal);
        job.setProposals(proposals);

        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal));
        when(jobService.getById(1L)).thenReturn(job);

        Proposal result = proposalService.accept(1L);

        assertNotNull(result);
        assertEquals(ProposalStatus.ACCEPTED, result.getStatus());
        verify(proposalRepository, times(1)).save(proposal);
    }

    @Test
    void testRejectProposal() {
        Proposal proposal = new Proposal();
        proposal.setId(1L);
        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal));

        Proposal result = proposalService.reject(1L);

        assertNotNull(result);
        assertEquals(ProposalStatus.REJECTED, result.getStatus());
        verify(proposalRepository, times(1)).save(proposal);
    }

    @Test
    void testGetAllForFreelancer() {
        List<Proposal> proposals = List.of(new Proposal(), new Proposal());
        UserRepresentation userRepresentation = new UserRepresentation("freelancer1");

        when(keycloakFeignClient.getUser()).thenReturn(userRepresentation);
        when(proposalRepository.findAllByFreelancerId("freelancer1")).thenReturn(proposals);

        List<Proposal> result = proposalService.getAllFroFreelancer();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(proposalRepository, times(1)).findAllByFreelancerId("freelancer1");
    }
}
