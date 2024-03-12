package com.bsuir.service;

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
        freelancerService.validateExistById(resume.getFreelancer().getId());

        return resumeRepository.save(resume);
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