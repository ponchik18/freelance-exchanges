package com.bsuir.repository;

import com.bsuir.entity.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    List<Proposal> findAllByFreelancerId(String freelancerId);
}