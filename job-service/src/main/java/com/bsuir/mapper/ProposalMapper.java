package com.bsuir.mapper;

import com.bsuir.dto.proposal.ProposalRequest;
import com.bsuir.dto.proposal.ProposalResponse;
import com.bsuir.entity.Proposal;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProposalMapper {

    ProposalResponse toDto(Proposal proposal);
    @Mapping(target = "job.id", source = "jobId")
    Proposal toEntity(ProposalRequest proposalRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Proposal partialUpdate(ProposalResponse proposalResponse, @MappingTarget Proposal proposal);
}