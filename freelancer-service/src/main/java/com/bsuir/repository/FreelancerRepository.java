package com.bsuir.repository;

import com.bsuir.entity.Freelancer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FreelancerRepository extends JpaRepository<Freelancer, Long> {
    Optional<Freelancer> findByEmailAndUserIdNot(String email, String id);

    Boolean existsByEmail(String email);

    Optional<Freelancer> findByUserId(String userId);
}