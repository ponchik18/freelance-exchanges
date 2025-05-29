package com.bsuir.repository;

import com.bsuir.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Set;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    List<Job> findAllByCustomerId(String customerId);
    Set<Job> findAllByProposalsFreelancerId(String proposalsFreelancerId);
    Long countBySkillsId(Long skillId);
}