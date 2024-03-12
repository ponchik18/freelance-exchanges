package com.bsuir.mapper;

import com.bsuir.dto.FreelancerRequest;
import com.bsuir.dto.FreelancerResponse;
import com.bsuir.dto.FreelancerUpdateRequest;
import com.bsuir.entity.Freelancer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = ResumeMapper.class)
public interface FreelancerMapper {
    FreelancerResponse toDto(Freelancer freelancer);
    Freelancer toEntityWhenCreate(FreelancerRequest freelancerRequest);
    Freelancer toEntityWhenUpdate(FreelancerUpdateRequest freelancerUpdateRequest);
}