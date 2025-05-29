package com.bsuir.controller;

import com.bsuir.dto.job.*;
import com.bsuir.dto.kafka.Message;
import com.bsuir.dto.kafka.MessageStatus;
import com.bsuir.dto.proposal.ProposalResponse;
import com.bsuir.dto.skill.SkillForAnalyticsResponse;
import com.bsuir.entity.Job;
import com.bsuir.entity.Proposal;
import com.bsuir.enums.JobStatus;
import com.bsuir.enums.ProposalStatus;
import com.bsuir.mapper.JobResponseMapper;
import com.bsuir.mapper.JobSimpleMapper;
import com.bsuir.repository.JobRepository;
import com.bsuir.service.JobService;
import com.bsuir.service.KafkaNotificationService;
import com.bsuir.util.PageSetting;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("jobs")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;
    private final JobSimpleMapper jobMapper;
    private final JobResponseMapper jobResponseMapper;
    private final KafkaNotificationService kafkaNotificationService;
    private final JobRepository jobRepository;

    @GetMapping("{id}")
    public JobResponse getJobById(@PathVariable Long id) {
        Job job = jobService.getById(id);
        return jobResponseMapper.apply(job);
    }

    @GetMapping
    public Page<JobResponse> getAllJob(JobFilter jobFilter, PageSetting pageSetting) {
        Page<Job> jobs = jobService.getAll(jobFilter, pageSetting);
        return jobs.map(jobResponseMapper);
    }

    @GetMapping("/smart-search/{freelancerId}")
    public Page<JobResponse> getAllJobs(@PathVariable String freelancerId, JobFilter jobFilter, PageSetting pageSetting) {
        Page<Job> jobs = jobService.searchSmart(freelancerId, jobFilter, pageSetting);
        return jobs.map(jobResponseMapper);
    }

    @GetMapping("/customer/{customerId}")
    public List<JobSimpleResponse> getAllJobForCustomer(@PathVariable String customerId) {
        List<Job> jobs = jobService.getAllJobForCustomer(customerId);
        return jobs.stream()
                .map(jobMapper::toResponse)
                .toList();
    }

    @GetMapping("/freelancer/{freelancerId}")
    public FreelancerJobsResponse getAllJobForFreelancer(@PathVariable String freelancerId) {
        List<Job> jobs = jobService.getAllJobForFreelancer(freelancerId);
        List<JobResponse> allJobs = jobs.stream()
                .map(jobResponseMapper)
                .toList();
        Map<JobStatus, List<JobResponse>> jobStatusListMap = allJobs.stream()
                .collect(Collectors.groupingBy(JobResponse::getJobStatus));

        return FreelancerJobsResponse.builder()
                .allJobs(allJobs)
                .workedJobs(jobStatusListMap.get(JobStatus.WORKED))
                .cancelledJobs(jobStatusListMap.get(JobStatus.CANCELLED))
                .finishJobs(jobStatusListMap.get(JobStatus.FINISH))
                .paidJobs(jobStatusListMap.get(JobStatus.PAID))
                .createdJobs(jobStatusListMap.get(JobStatus.CREATED))
                .build();
    }

    @PutMapping("/start/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public JobResponse startJob(@PathVariable long id) {
        Job createdJob = jobService.startJob(id);
        JobResponse fullJobResponse = jobResponseMapper.apply(createdJob);
        kafkaNotificationService.sendMessageToCustomer(MessageStatus.JOB_STARTED, fullJobResponse);
        kafkaNotificationService.sendMessageToFreelancer(MessageStatus.JOB_STARTED, fullJobResponse);
        return fullJobResponse;
    }

    @PutMapping("/finish/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public JobResponse finishJob(@PathVariable long id, @RequestParam("file") MultipartFile file) {
        Job createdJob = jobService.finish(id, file);
        JobResponse fullJobResponse = jobResponseMapper.apply(createdJob);
        kafkaNotificationService.sendMessageToCustomer(MessageStatus.JOB_FINISHED, fullJobResponse);
        kafkaNotificationService.sendMessageToFreelancer(MessageStatus.JOB_FINISHED, fullJobResponse);

        return fullJobResponse;
    }

    @PutMapping("/pay/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void payJob(@PathVariable long id) {
        Job job = jobService.payJob(id);
        JobResponse fullJobResponse = jobResponseMapper.apply(job);
        kafkaNotificationService.sendMessageToCustomer(MessageStatus.JOB_PAID, fullJobResponse);
        kafkaNotificationService.sendMessageToFreelancer(MessageStatus.JOB_PAID, fullJobResponse);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobResponse createJob(@RequestBody @Valid JobRequest jobRequest) {
        Job job = jobMapper.toEntity(jobRequest);
        Job createdJob = jobService.create(job, jobRequest.getSkills());
        return jobResponseMapper.apply(createdJob);
    }

    @PatchMapping("{id}")
    public JobResponse updateJob(@PathVariable Long id, @RequestBody @Valid JobRequest jobRequest) {
        Job job = jobMapper.toEntity(jobRequest);
        job.setId(id);
        Job updatedJob = jobService.update(job);
        return jobResponseMapper.apply(updatedJob);
    }

    @GetMapping("/analytics/skills")
    public List<SkillForAnalyticsResponse> generateSkillsForAnalytics() {
        return jobService.generateSkillsForAnalytics();
    }
}