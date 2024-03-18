package com.bsuir.controller;

import com.bsuir.dto.FreelancerRequest;
import com.bsuir.dto.FreelancerResponse;
import com.bsuir.dto.FreelancerUpdateRequest;
import com.bsuir.entity.Freelancer;
import com.bsuir.mapper.FreelancerMapper;
import com.bsuir.service.FreelancerService;
import com.bsuir.util.PageSetting;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("freelancers")
@RequiredArgsConstructor
public class FreelancerController {
    private final FreelancerService freelancerService;
    private final FreelancerMapper freelancerMapper;

    @GetMapping
    public Page<FreelancerResponse> getAllFreelancer(PageSetting pageSetting) {
        Page<Freelancer> freelancers = freelancerService.getAll(pageSetting);
        return freelancers.map(freelancerMapper::toDto);
    }

    @GetMapping("{id}")
    public FreelancerResponse getFreelancerById(@PathVariable String id) {
        Freelancer freelancer = freelancerService.getById(id);
        return freelancerMapper.toDto(freelancer);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FreelancerResponse createFreelancer(@RequestBody @Valid FreelancerRequest freelancerRequest) {
        Freelancer freelancer = freelancerMapper.toEntityWhenCreate(freelancerRequest);
        Freelancer savedFreelancer = freelancerService.save(freelancer);
        return freelancerMapper.toDto(savedFreelancer);
    }

    @PutMapping("{id}")
    public FreelancerResponse updateFreelancer(@PathVariable String id, @RequestBody @Valid FreelancerUpdateRequest freelancerUpdateRequest) {
        Freelancer freelancer = freelancerMapper.toEntityWhenUpdate(freelancerUpdateRequest);
        Freelancer updatedFreelancer = freelancerService.update(id, freelancer);
        return freelancerMapper.toDto(updatedFreelancer);
    }
}