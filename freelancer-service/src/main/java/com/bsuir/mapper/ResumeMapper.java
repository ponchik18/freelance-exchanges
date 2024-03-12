package com.bsuir.mapper;

import com.bsuir.dto.ResumeRequest;
import com.bsuir.dto.ResumeResponse;
import com.bsuir.dto.ResumeUpdateRequest;
import com.bsuir.entity.Resume;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ResumeMapper {

    @Mapping(target = "freelancerId", source = "freelancer.id")
    ResumeResponse toDto(Resume resume);

    List<ResumeResponse> toListOfDto(List<Resume> resumeList);
    @Mapping(target = "freelancer.id", source = "freelancerId")
    Resume toEntityWhenCreate(ResumeRequest resumeRequest);
    Resume toEntityWhenUpdate(ResumeUpdateRequest resumeUpdateRequest);
}