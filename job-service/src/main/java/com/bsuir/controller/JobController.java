package com.bsuir.controller;

import com.bsuir.dto.job.JobRequest;
import com.bsuir.dto.job.JobResponse;
import com.bsuir.entity.Job;
import com.bsuir.mapper.JobMapper;
import com.bsuir.mapper.JobResponseMapper;
import com.bsuir.service.JobService;
import com.bsuir.util.PageSetting;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("jobs")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;
    private final JobMapper jobMapper;
    private final JobResponseMapper jobResponseMapper;

    @GetMapping("{id}")
    public JobResponse getJobById(@PathVariable Long id) {
        Job job = jobService.getById(id);
        return jobResponseMapper.apply(job);
    }

    @GetMapping
    public Page<JobResponse> getAllJob(PageSetting pageSetting) {
        Page<Job> jobs = jobService.getAll(pageSetting);
        return jobs.map(jobResponseMapper);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobResponse createJob(@RequestBody @Valid JobRequest jobRequest) {
        Job job = jobMapper.toEntity(jobRequest);
        Job createdJob = jobService.create(job);
        return jobResponseMapper.apply(createdJob);
    }

    @PatchMapping("{id}")
    public JobResponse updateJob(@PathVariable Long id, @RequestBody @Valid JobRequest jobRequest) {
        Job job = jobMapper.toEntity(jobRequest);
        job.setId(id);
        Job updatedJob = jobService.update(job);
        return jobResponseMapper.apply(updatedJob);
    }

}