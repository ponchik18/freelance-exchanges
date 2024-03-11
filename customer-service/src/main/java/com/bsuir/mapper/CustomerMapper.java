package com.bsuir.mapper;

import com.bsuir.dto.CustomerCreateRequest;
import com.bsuir.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {
    Customer toEntity(CustomerCreateRequest customerCreateRequest);
}