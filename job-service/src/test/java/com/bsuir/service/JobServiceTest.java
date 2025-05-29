package com.bsuir.service;

import com.bsuir.dto.job.JobFilter;
import com.bsuir.dto.user.UserRepresentation;
import com.bsuir.entity.Job;
import com.bsuir.entity.Skill;
import com.bsuir.enums.JobStatus;
import com.bsuir.exception.JobHasAlreadyClosedException;
import com.bsuir.exception.JobNotFoundException;
import com.bsuir.feign.ChatFeignClient;
import com.bsuir.feign.FreelancerFeignClient;
import com.bsuir.feign.KeycloakFeignClient;
import com.bsuir.feign.StorageFeignClient;
import com.bsuir.repository.JobRepository;
import com.bsuir.repository.SkillFreelancerRepository;
import com.bsuir.repository.SkillRepository;
import com.bsuir.util.JobSpecification;
import com.bsuir.util.PageSetting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JobServiceTest {

    @InjectMocks
    private JobService jobService;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private KeycloakFeignClient keycloakFeignClient;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private ChatFeignClient chatFeignClient;

    @Mock
    private StorageFeignClient storageFeignClient;

    @Mock
    private FreelancerFeignClient freelancerFeignClient;

    @Mock
    private SkillFreelancerRepository skillFreelancerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetById_WhenJobExists() {
        Job job = new Job();
        job.setId(1L);
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));

        Job result = jobService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(jobRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_WhenJobDoesNotExist() {
        when(jobRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(JobNotFoundException.class, () -> jobService.getById(1L));
        verify(jobRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAll() throws InterruptedException {
        JobFilter jobFilter = new JobFilter();
        PageSetting pageSetting = new PageSetting(0, 10);
        Pageable pageable = PageRequest.of(0, 10);
        List<Job> jobs = new ArrayList<>();
        jobs.add(new Job());
        Page<Job> jobPage = new PageImpl<>(jobs);

        when(jobRepository.findAll((Specification<Job>) any(JobSpecification.class), eq(pageable))).thenReturn(jobPage);

        Page<Job> result = jobService.getAll(jobFilter, pageSetting);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(jobRepository, times(1)).findAll((Specification<Job>) any(JobSpecification.class), eq(pageable));
    }

    @Test
    void testCreate() throws InterruptedException {
        Job job = new Job();
        job.setId(1L);
        List<Long> skillIds = List.of(1L, 2L);
        UserRepresentation user = new UserRepresentation();
        user.setUserId("123");

        when(keycloakFeignClient.getUser()).thenReturn(user);
        when(jobRepository.save(any(Job.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Skill parentSkill = new Skill(3L, "DEV", null);
        when(skillRepository.findById(1L)).thenReturn(Optional.of(new Skill(1L, "Java", parentSkill)));
        when(skillRepository.findById(2L)).thenReturn(Optional.of(new Skill(2L, "Spring", parentSkill)));

        Job result = jobService.create(job, skillIds);

        assertNotNull(result);
        assertEquals(JobStatus.CREATED, result.getStatus());
        assertEquals("123", result.getCustomerId());
        assertEquals(2, result.getSkills().size());
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    void testUpdate_WhenJobIsNotFinished() {
        Job job = new Job();
        job.setId(1L);
        job.setStatus(JobStatus.CREATED);
        job.setTitle("Updated Title");

        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(jobRepository.save(any(Job.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Job result = jobService.update(job);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        verify(jobRepository, times(1)).save(job);
    }

    @Test
    void testUpdate_WhenJobIsFinished() {
        Job job = new Job();
        job.setId(1L);
        job.setStatus(JobStatus.FINISH);

        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));

        assertThrows(JobHasAlreadyClosedException.class, () -> jobService.update(job));
        verify(jobRepository, times(0)).save(job);
    }
}
