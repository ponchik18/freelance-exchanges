package com.bsuir.service;

import com.bsuir.entity.Job;
import com.bsuir.entity.Skill;
import com.bsuir.entity.SkillFreelancer;
import com.bsuir.exception.SkillForFreelancerNotFoundException;
import com.bsuir.exception.SkillNotFoundException;
import com.bsuir.feign.FreelancerFeignClient;
import com.bsuir.repository.SkillFreelancerRepository;
import com.bsuir.repository.SkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SkillServiceTest {

    @InjectMocks
    private SkillService skillService;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private SkillFreelancerRepository skillFreelancerRepository;

    @Mock
    private FreelancerFeignClient freelancerFeignClient;

    @Mock
    private JobService jobService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        List<Skill> skills = List.of(new Skill(1L, "Java", null), new Skill(2L, "Spring", null));
        when(skillRepository.findAll()).thenReturn(skills);

        List<Skill> result = skillService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(skillRepository, times(1)).findAll();
    }

    @Test
    void testGetById_WhenSkillExists() {
        Skill skill = new Skill(1L, "Java", null);
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));

        Skill result = skillService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(skillRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_WhenSkillDoesNotExist() {
        when(skillRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SkillNotFoundException.class, () -> skillService.getById(1L));
        verify(skillRepository, times(1)).findById(1L);
    }

    @Test
    void testAddSkillToFreelancer_WhenSkillDoesNotExistForFreelancer() {
        String freelancerId = "freelancer1";
        Skill skill = new Skill(1L, "Java", null);

        when(freelancerFeignClient.getFreelancerById(freelancerId)).thenReturn(null);
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));
        when(skillFreelancerRepository.findByFreelancerIdAndSkillId(freelancerId, 1L))
                .thenReturn(Optional.empty());

        Skill result = skillService.addSkillToFreelancer(freelancerId, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(skillRepository, times(1)).findById(1L);
        verify(skillFreelancerRepository, times(1)).save(any(SkillFreelancer.class));
    }

    @Test
    void testDeleteSkillFromFreelancer_WhenSkillExists() {
        String freelancerId = "freelancer1";
        Skill skill = new Skill(1L, "Java", null);
        SkillFreelancer skillFreelancer = new SkillFreelancer(1L, freelancerId, skill);

        when(skillFreelancerRepository.findByFreelancerIdAndSkillId(freelancerId, 1L))
                .thenReturn(Optional.of(skillFreelancer));

        skillService.deleteSkillFromFreelancer(freelancerId, 1L);

        verify(skillFreelancerRepository, times(1)).delete(skillFreelancer);
    }

    @Test
    void testDeleteSkillFromFreelancer_WhenSkillDoesNotExist() {
        String freelancerId = "freelancer1";

        when(skillFreelancerRepository.findByFreelancerIdAndSkillId(freelancerId, 1L))
                .thenReturn(Optional.empty());

        assertThrows(SkillForFreelancerNotFoundException.class,
                () -> skillService.deleteSkillFromFreelancer(freelancerId, 1L));
        verify(skillFreelancerRepository, times(0)).delete(any(SkillFreelancer.class));
    }

    @Test
    void testAddSkillToJob_WhenSkillNotExistForJob() {
        Long jobId = 1L;
        Long skillId = 2L;
        Skill skill = new Skill(skillId, "Java", null);
        Job job = new Job();
        job.setId(jobId);
        job.setSkills(new ArrayList<>());

        when(skillRepository.findById(skillId)).thenReturn(Optional.of(skill));
        when(jobService.getById(jobId)).thenReturn(job);

        Skill result = skillService.addSkillToJob(jobId, skillId);

        assertNotNull(result);
        assertEquals(skillId, result.getId());
        verify(skillRepository, times(1)).findById(skillId);
        verify(jobService, times(1)).update(job);
    }

    @Test
    void testDeleteSkillFromJob_WhenSkillExists() {
        Long jobId = 1L;
        Long skillId = 2L;
        Skill skill = new Skill(skillId, "Java", null);
        Job job = new Job();
        job.setId(jobId);
        job.setSkills(new ArrayList<>(List.of(skill)));

        when(jobService.getById(jobId)).thenReturn(job);

        skillService.deleteSkillFromJob(jobId, skillId);

        assertTrue(job.getSkills().isEmpty());
        verify(jobService, times(1)).update(job);
    }
}
