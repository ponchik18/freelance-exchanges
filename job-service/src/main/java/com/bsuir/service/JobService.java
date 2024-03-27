package com.bsuir.service;

import com.bsuir.dto.user.UserRepresentation;
import com.bsuir.entity.Job;
import com.bsuir.enums.JobStatus;
import com.bsuir.exception.JobHasAlreadyClosedException;
import com.bsuir.exception.JobNotFoundException;
import com.bsuir.feign.KeycloakFeignClient;
import com.bsuir.repository.JobRepository;
import com.bsuir.util.PageSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;
    private final KeycloakFeignClient keycloakFeignClient;

    public Job getById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(()-> new JobNotFoundException(id));
    }

    public Page<Job> getAll(PageSetting pageSetting) {
        Pageable pageable = PageRequest.of(pageSetting.getPageNumber(), pageSetting.getElementsPerPage());
        return jobRepository.findAll(pageable);
    }

    public Job create(Job job) {
        job.setStatus(JobStatus.CREATED);
        UserRepresentation userRepresentation = keycloakFeignClient.getUser();
        job.setCustomerId(userRepresentation.getId());

        return jobRepository.save(job);
    }

    public Job update(Job job) {
        Job updatedJob = getById(job.getId());
        if(updatedJob.getStatus() != JobStatus.FINISH) {
            throw new JobHasAlreadyClosedException(updatedJob.getId());
        }
        updatedJob.setTitle(job.getTitle());
        updatedJob.setBudget(job.getBudget());
        updatedJob.setDescription(job.getDescription());
        return jobRepository.save(job);
    }
}