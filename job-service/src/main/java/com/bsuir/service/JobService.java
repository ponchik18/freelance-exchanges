package com.bsuir.service;

import com.bsuir.dto.job.JobFilter;
import com.bsuir.dto.message.ChatRequest;
import com.bsuir.dto.skill.SkillForAnalyticsResponse;
import com.bsuir.dto.user.UserRepresentation;
import com.bsuir.entity.Job;
import com.bsuir.entity.Skill;
import com.bsuir.enums.JobStatus;
import com.bsuir.enums.ProposalStatus;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;
    private final KeycloakFeignClient keycloakFeignClient;
    private final SkillRepository skillRepository;
    private final ChatFeignClient chatFeignClient;
    private final StorageFeignClient storageFeignClient;
    private final FreelancerFeignClient freelancerFeignClient;
    private final SkillFreelancerRepository skillFreelancerRepository;

    public Job getById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException(id));
    }

    public Page<Job> getAll(JobFilter jobFilter, PageSetting pageSetting) {
        Pageable pageable = PageRequest.of(pageSetting.getPageNumber(), pageSetting.getElementsPerPage());

        return jobRepository.findAll(JobSpecification.createSpecification(jobFilter), pageable);
    }

    public Page<Job> searchSmart(String freelancerId, JobFilter jobFilter, PageSetting pageSetting) {
        freelancerFeignClient.getFreelancerById(freelancerId);
        List<Long>  skills = skillFreelancerRepository.findSkillByFreelancerId(freelancerId)
                .stream()
                .map(Skill::getId)
                .toList();
        List<Long> searchSkills = jobFilter.getSkillIds();
        if(searchSkills == null) {
            jobFilter.setSkillIds(skills);
        } else if (searchSkills.isEmpty()) {
            jobFilter.setSkillIds(skills);
        }

        Pageable pageable = PageRequest.of(pageSetting.getPageNumber(), pageSetting.getElementsPerPage());
        return jobRepository.findAll(JobSpecification.createSpecification(jobFilter), pageable);
    }

    public Job create(Job job, List<Long> skillsIds) {
        job.setStatus(JobStatus.CREATED);
        UserRepresentation userRepresentation = keycloakFeignClient.getUser();
        job.setCustomerId(userRepresentation.getUserId());

        Job savedJob = jobRepository.save(job);
        savedJob.setSkills(new ArrayList<Skill>());
        if (skillsIds != null && !skillsIds.isEmpty()) {
            for (Long skillId : skillsIds) {
                addSkillToJob(savedJob.getId(), skillId);
            }
        }
        return savedJob;
    }

    public Job update(Job job) {
        Job updatedJob = getById(job.getId());
        if (updatedJob.getStatus() == JobStatus.FINISH) {
            throw new JobHasAlreadyClosedException(updatedJob.getId());
        }
        updatedJob.setTitle(job.getTitle());
        updatedJob.setBudget(job.getBudget());
        updatedJob.setDescription(job.getDescription());
        return jobRepository.save(job);
    }

    public List<Job> getAllJobForCustomer(String customerId) {
        return jobRepository.findAllByCustomerId(customerId);
    }

    private void addSkillToJob(Long jobId, Long skillId) {
        Skill skill = skillRepository.findById(skillId).orElse(null);
        if (skill == null) {
            return;
        }

        Job job = getById(jobId);
        if (job.getSkills() == null) {
            job.setSkills(new ArrayList<>());
        }
        job.getSkills().add(skill);
        jobRepository.save(job);
    }

    public Job startJob(long id) {
        Job job = jobRepository.findById(id).orElse(null);

        assert job != null;
        String from = job.getProposals().stream()
                .filter((proposal -> proposal.getStatus() == ProposalStatus.ACCEPTED))
                .findFirst().orElseThrow().getFreelancerId();
        chatFeignClient.createMessage(ChatRequest.builder()
                .to(job.getCustomerId())
                .from(from)
                .build());
        return updateStatus(id, JobStatus.WORKED);
    }

    public Job finish(long id, MultipartFile file) {
        Job job = getById(id);
        Map<String, String> uploadedFile = storageFeignClient.uploadFile(file);
        job.setJobReference(uploadedFile.get("filePath"));
        jobRepository.save(job);
        return updateStatus(id, JobStatus.FINISH);
    }

    public Job payJob(long id) {
        return updateStatus(id, JobStatus.PAID);
    }

    private Job updateStatus(long id, JobStatus jobStatus) {
        Job job = getById(id);
        job.setStatus(jobStatus);
        return jobRepository.save(job);
    }

    public List<Job> getAllJobForFreelancer(String freelancerId) {
        return jobRepository.findAllByProposalsFreelancerId(freelancerId)
                .stream().toList();
    }

    public List<SkillForAnalyticsResponse> generateSkillsForAnalytics() {
        List<Skill> skills = skillRepository.findAll()
                .stream()
                .filter(skill -> skill.getParent() != null)
                .toList();
        List<SkillForAnalyticsResponse> responses = new ArrayList<>();
        long jobCount = jobRepository.count();
        for(Skill skill : skills) {
            Long countBySkill = jobRepository.countBySkillsId(skill.getId());
            responses.add(SkillForAnalyticsResponse.builder()
                            .skill(skill)
                            .count(countBySkill)
                            .percent(
                                    BigDecimal.valueOf(countBySkill).divide(
                                            BigDecimal.valueOf(jobCount),
                                            2,
                                            MathContext.DECIMAL64.getRoundingMode())
                            )
                    .build());
        }
        responses.sort(Comparator.comparing(SkillForAnalyticsResponse::getCount).reversed());
        return responses;
    }
}