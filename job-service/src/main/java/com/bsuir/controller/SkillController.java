package com.bsuir.controller;

import com.bsuir.dto.skill.SkillRequest;
import com.bsuir.dto.skill.SkillResponse;
import com.bsuir.dto.skill.SkillWithParentResponse;
import com.bsuir.entity.Skill;
import com.bsuir.mapper.SkillMapper;
import com.bsuir.service.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("skills")
public class SkillController {
    private final SkillService skillService;
    private final SkillMapper skillMapper;

    @GetMapping("{id}")
    public SkillResponse getById(@PathVariable Long id) {
        Skill skill = skillService.getById(id);
        return skillMapper.toResponse(skill);
    }

    @GetMapping
    public List<SkillWithParentResponse> getAll() {
        List<Skill> skills = skillService.getAll();

        return getSkillWithParentResponses(skills);
    }

    private static List<SkillWithParentResponse> getSkillWithParentResponses(List<Skill> skills) {
        Map<Skill, List<Skill>> groupedSkills = skills.stream()
                .filter(skill -> skill.getParent() != null)
                .collect(Collectors.groupingBy(Skill::getParent));
        return groupedSkills.entrySet().stream()
                .map(entry -> new SkillWithParentResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SkillResponse createSkill(@RequestBody @Valid SkillRequest skillRequest) {
        Skill skill = skillMapper.toEntity(skillRequest);
        Skill savedSkill = skillService.save(skill);
        return skillMapper.toResponse(savedSkill);
    }

    @PutMapping("{id}")
    public SkillResponse updateSkill(@PathVariable Long id, @RequestBody @Valid SkillRequest skillRequest) {
        Skill skill = skillMapper.toEntity(skillRequest);
        skill.setId(id);
        Skill updatedSkill = skillService.save(skill);
        return skillMapper.toResponse(updatedSkill);
    }

    @GetMapping("/freelancer/{id}")
    public List<SkillResponse> getAllSkillForFreelancer(@PathVariable String id) {
        List<Skill> skills = skillService.getAllForFreelancer(id);
        return skills.stream()
                .map(skillMapper::toResponse)
                .toList();
    }

    @GetMapping("/freelancer/for-smart-search/{id}")
    public List<SkillWithParentResponse> getAllSkillForFreelancerForSmartSearch(@PathVariable String id) {
        List<Skill> skills = skillService.getAllForFreelancer(id);

        return getSkillWithParentResponses(skills);
    }

    @GetMapping("/not-selected/freelancer/{id}")
    public List<SkillWithParentResponse> getAllSkillForFreelancerNotSelected(@PathVariable String id) {
        List<Skill> skills = skillService.getAllSkillForFreelancerNotSelected(id);
        return getSkillWithParentResponses(skills);
    }

    @PostMapping("/freelancer/{freelancerId}/{skillId}")
    @ResponseStatus(HttpStatus.CREATED)
    public SkillResponse addSkillToFreelancer(@PathVariable String freelancerId, @PathVariable Long skillId) {
        Skill skill = skillService.addSkillToFreelancer(freelancerId, skillId);
        return skillMapper.toResponse(skill);
    }

    @DeleteMapping("/freelancer/{freelancerId}/{skillId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSkillFromFreelancer(@PathVariable String freelancerId, @PathVariable Long skillId) {
        skillService.deleteSkillFromFreelancer(freelancerId, skillId);
    }

    @PostMapping("/jobs/{jobId}/{skillId}")
    @ResponseStatus(HttpStatus.CREATED)
    public SkillResponse addSkillToJob(@PathVariable Long jobId, @PathVariable Long skillId) {
        Skill skill = skillService.addSkillToJob(jobId, skillId);
        return skillMapper.toResponse(skill);
    }

    @DeleteMapping("/jobs/{jobId}/{skillId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSkillFromJob(@PathVariable Long jobId, @PathVariable Long skillId) {
        skillService.deleteSkillFromJob(jobId, skillId);
    }



}