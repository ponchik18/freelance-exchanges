package com.bsuir.mapper;

import com.bsuir.dto.FreelancerRequest;
import com.bsuir.dto.FreelancerResponse;
import com.bsuir.entity.Freelancer;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = ResumeMapper.class)
public interface FreelancerMapper {
    FreelancerResponse toDto(Freelancer freelancer);
    FreelancerRequest toEntity(FreelancerRequest freelancerRequest);
}