package com.bsuir.repository;

import com.bsuir.entity.Skill;
import com.bsuir.entity.SkillFreelancer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SkillFreelancerRepository extends JpaRepository<SkillFreelancer, Long> {
    @Query("SELECT Skill FROM SkillFreelancer sf WHERE sf.freelancerId = ?1")
    List<Skill> findSkillByFreelancerId(String freelancerId);
    Optional<SkillFreelancer> findByFreelancerIdAndSkillId(String freelancerId, Long skillId);
}