package com.bsuir.controller;

import com.bsuir.dto.proposal.ProposalRequest;
import com.bsuir.dto.proposal.ProposalResponse;
import com.bsuir.entity.Proposal;
import com.bsuir.mapper.ProposalMapper;
import com.bsuir.service.ProposalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("proposals")
@RequiredArgsConstructor
public class ProposalController {
    private ProposalService proposalService;
    private ProposalMapper proposalMapper;

    @GetMapping("{id}")
    public ProposalResponse getById(@PathVariable long id) {
        Proposal proposal = proposalService.getById(id);
        return proposalMapper.toDto(proposal);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProposalResponse create(@RequestBody @Valid ProposalRequest proposalRequest) {
        Proposal proposal = proposalMapper.toEntity(proposalRequest);
        Proposal createdProposal = proposalService.create(proposal);
        return proposalMapper.toDto(createdProposal);
    }

    @PatchMapping("/accept/{id}")
    public ProposalResponse accept(@PathVariable long id) {
        Proposal updatedProposal = proposalService.accept(id);
        return proposalMapper.toDto(updatedProposal);
    }

    @PatchMapping("/reject/{id}")
    public ProposalResponse reject(@PathVariable long id) {
        Proposal updatedProposal = proposalService.reject(id);
        return proposalMapper.toDto(updatedProposal);
    }

    @GetMapping
    public List<ProposalResponse> getAllForFreelancer() {
        List<Proposal> proposals = proposalService.getAllFroFreelancer();
        return proposals.stream()
                .map(proposalMapper::toDto)
                .toList();
    }
}