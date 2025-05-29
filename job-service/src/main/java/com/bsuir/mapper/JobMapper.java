package com.bsuir.mapper;

import com.bsuir.dto.job.JobRequest;
import com.bsuir.dto.job.JobResponse;
import com.bsuir.entity.Job;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface JobMapper {
    @Mapping(target = "skills", source = "skills", ignore = true)
    Job toEntity(JobRequest JobRequest);


    JobResponse toResponse(Job job);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Job partialUpdate(JobResponse jobResponse, @MappingTarget Job job);
}