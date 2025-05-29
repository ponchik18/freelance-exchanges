package com.bsuir.service;

import com.bsuir.entity.Job;
import com.bsuir.entity.Skill;
import com.bsuir.entity.SkillFreelancer;
import com.bsuir.exception.SkillForFreelancerNotFoundException;
import com.bsuir.exception.SkillNotFoundException;
import com.bsuir.feign.FreelancerFeignClient;
import com.bsuir.repository.SkillFreelancerRepository;
import com.bsuir.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillService {
    private final SkillRepository skillRepository;
    private final SkillFreelancerRepository skillFreelancerRepository;
    private final FreelancerFeignClient freelancerFeignClient;
    private final JobService jobService;

    public List<Skill> getAll() {
        return skillRepository.findAll();
    }

    public Skill getById(Long id) {
        return skillRepository.findById(id)
                .orElseThrow(() -> new SkillNotFoundException(id));
    }

    public Skill save(Skill skill) {
        return skillRepository.save(skill);
    }

    public List<Skill> getAllForFreelancer(String id) {
        return skillFreelancerRepository.findSkillByFreelancerId(id);
    }

    public Skill addSkillToFreelancer(String freelancerId, Long skillId) {
        validateFreelancerExist(freelancerId);
        Skill skill = getById(skillId);
        if (skillFreelancerRepository.findByFreelancerIdAndSkillId(freelancerId, skillId)
                .isEmpty()) {
            SkillFreelancer skillFreelancer = SkillFreelancer.builder()
                    .freelancerId(freelancerId)
                    .skill(skill)
                    .build();

            skillFreelancerRepository.save(skillFreelancer);
        }
        return skill;
    }

    public void deleteSkillFromFreelancer(String freelancerId, Long skillId) {
        SkillFreelancer skillFreelancer = skillFreelancerRepository.findByFreelancerIdAndSkillId(freelancerId, skillId)
                .orElseThrow(() -> new SkillForFreelancerNotFoundException(freelancerId, skillId));

        skillFreelancerRepository.delete(skillFreelancer);
    }

    private void validateFreelancerExist(String freelancerId) {
        freelancerFeignClient.getFreelancerById(freelancerId);
    }

    public Skill addSkillToJob(Long jobId, Long skillId) {
        Skill skill = getById(skillId);

        Job job = jobService.getById(jobId);
        if (!isSkillForJobExist(jobId, job)) {
            job.getSkills().add(skill);
            jobService.update(job);
        }

        return skill;
    }

    private boolean isSkillForJobExist(Long jobId, Job job) {
        return job.getSkills().stream()
                .map(Skill::getId)
                .anyMatch(jobId::equals);
    }

    public void deleteSkillFromJob(Long jobId, Long skillId) {
        Job job = jobService.getById(jobId);
        List<Skill> skills = job.getSkills();
        skills.stream()
                .filter(skill -> skillId.equals(skill.getId()))
                .findFirst()
                .ifPresent(skills::remove);

        job.setSkills(skills);
        jobService.update(job);
    }

    public List<Skill> getAllSkillForFreelancerNotSelected(String id) {
        List<Skill> allSkill = getAll();
        List<Long> freelancerSkillIds = getAllForFreelancer(id).stream()
                .map(Skill::getId)
                .toList();
        return allSkill.stream()
                .filter(skill -> !freelancerSkillIds.contains(skill.getId()))
                .collect(Collectors.toList());
    }
}