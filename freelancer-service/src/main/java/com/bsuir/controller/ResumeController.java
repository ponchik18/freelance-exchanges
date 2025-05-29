package com.bsuir.controller;

import com.bsuir.dto.ResumeRequest;
import com.bsuir.dto.ResumeResponse;
import com.bsuir.dto.ResumeUpdateRequest;
import com.bsuir.entity.Resume;
import com.bsuir.mapper.ResumeMapper;
import com.bsuir.service.ResumeService;
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

@RestController
@RequestMapping("resumes")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeMapper resumeMapper;
    private final ResumeService resumeService;

    @GetMapping("{id}")
    public ResumeResponse getResumeById(@PathVariable long id) {
        Resume resume = resumeService.getById(id);
        return resumeMapper.toDto(resume);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResumeResponse createOrUpdateResume(@RequestBody @Valid ResumeRequest resumeRequest) {
        Resume resume = resumeMapper.toEntityWhenCreate(resumeRequest);
        Resume savedResume = resumeService.save(resume);
        return resumeMapper.toDto(savedResume);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteResume(@PathVariable long id) {
        resumeService.deleteById(id);
    }

    @PutMapping("{id}")
    public ResumeResponse updateResume(@PathVariable long id, @RequestBody @Valid ResumeUpdateRequest resumeUpdateRequest) {
        Resume resume = resumeMapper.toEntityWhenUpdate(resumeUpdateRequest);
        Resume updatedResume = resumeService.update(id, resume);
        return resumeMapper.toDto(updatedResume);
    }
}