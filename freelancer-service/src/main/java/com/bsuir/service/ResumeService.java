package com.bsuir.service;

import com.bsuir.entity.Freelancer;
import com.bsuir.entity.Resume;
import com.bsuir.exception.ResumeNotFoundException;
import com.bsuir.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final FreelancerService freelancerService;

    public Resume getById(long id) {
        return resumeRepository.findById(id)
                .orElseThrow(() -> new ResumeNotFoundException(id));
    }

    public Resume save(Resume resume) {
        Freelancer freelancer = freelancerService.getById(resume.getFreelancer().getUserId());

        Resume createdResume = resumeRepository.findByFreelancerId(freelancer.getId())
                .orElseGet(()-> resume);
        createdResume.setResumeContent(resume.getResumeContent());
        createdResume.setResumeName(resume.getResumeName());
        createdResume.getFreelancer().setId(freelancer.getId());

        return resumeRepository.save(createdResume);
    }

    public void deleteById(long id) {
        if (!resumeRepository.existsById(id)) {
            throw new ResumeNotFoundException(id);
        }

        resumeRepository.deleteById(id);
    }

    public Resume update(long id, Resume resume) {
        Resume savedResume = getById(id);
        savedResume.setResumeName(resume.getResumeName());
        savedResume.setResumeContent(resume.getResumeContent());
        return resumeRepository.save(savedResume);
    }
}