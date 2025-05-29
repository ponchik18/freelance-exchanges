package com.bsuir.repository;

import com.bsuir.entity.Skill;
import com.bsuir.entity.SkillFreelancer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SkillFreelancerRepository extends JpaRepository<SkillFreelancer, Long> {

    @Query("SELECT sf.skill FROM SkillFreelancer sf WHERE sf.freelancerId = :freelancerId")
    List<Skill> findSkillByFreelancerId(@Param("freelancerId") String freelancerId);

    Optional<SkillFreelancer> findByFreelancerIdAndSkillId(String freelancerId, Long skillId);
}