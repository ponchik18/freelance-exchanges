package com.bsuir.mapper;

import com.bsuir.dto.ResumeRequest;
import com.bsuir.dto.ResumeResponse;
import com.bsuir.entity.Resume;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ResumeMapper {

    ResumeResponse toDto(Resume resume);

    List<ResumeResponse> toListOfDto(List<Resume> resumeList);
    @Mapping(target = "freelancer.id", source = "freelancerId")
    Resume toEntity(ResumeRequest resumeRequest);
}